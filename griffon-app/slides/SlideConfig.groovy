import griffon.builder.css.CSSDecorator
import griffon.builder.gfx.GfxContext
import griffon.builder.gfx.GfxNode
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImageOp
import javax.imageio.ImageIO
import javax.swing.JComponent
import java.awt.*

// Map to configure how the background should be painted
backgroundConfig = [
        // Set an instance of java.awt.Paint to paint the lowest background level
        // Paint may be a Paint or a Closure with one parameter (the Slide to paint to) returning a Paint
        paint: { slide -> getMasterPart(slide, 'paint') },
        // Add an image (below) or a GfxBuilder node to paint ontop of the given Paint
        // Node may be a GfxBuilder node or a Closure with one parameter (the Slide to paint to) returning a GfxNode
        node: { slide -> getMasterPart(slide, 'node') },
        // The image can be set as String (resource loaded from the classpath), URL, File, InputStream or Image
        // Image may aswell be a Closure with one parameter (the Slide to paint to) returning an Image
        image: { slide ->  getMasterPart(slide, 'image') },
        // java.awt.Composite used to draw the image
        // Composite may aswell be a Closure with one parameter (the Slide to paint to) returning a Composite
        composite: { slide ->  getMasterPart(slide, 'composite') },
        // java.awt.image.BufferedImageOp used to draw the image
        // Op may aswell be a Closure with one parameter (the Slide to paint to) returning a BufferedImageOp
        op: { slide ->  getMasterPart(slide, 'op') },
        // java.awt.geom.AffineTransform used to draw the image
        // Transform may aswell be a Closure with one parameter (the Slide to paint to) returning a AffineTransform
        transform: { slide ->  getMasterPart(slide, 'transform') }
]

getMasterPart = { slide, name, args = [] ->
    def master = this."master_${slide.master ?: 'Default'}"
    if(!master)
        return null
    def part = master."$name"
    if(part instanceof Closure)
        return part.call(*([slide] + args))
    else
        return part
} 

loadImage = { image ->
    if (image instanceof String)
        return ImageIO.read(Thread.currentThread().contextClassLoader.getResource(image))
    else if (image instanceof URL)
        return ImageIO.read(image)
    else if (image instanceof File)
        return ImageIO.read(image)
    else if (image instanceof InputStream)
        return ImageIO.read(image)
    else if (image instanceof Image)
        return image
    else
        return null
}

createCompatibleImage = {def width, def height ->
    GraphicsConfiguration gc = GraphicsEnvironment.localGraphicsEnvironment.defaultScreenDevice.defaultConfiguration
    gc.createCompatibleImage(width as int, height as int, Transparency.TRANSLUCENT as int)
}

backgroundPainter = { JComponent p, Graphics2D g ->
    Rectangle bounds = p.bounds
    g.clearRect(bounds.x as int, bounds.y as int, bounds.width as int, bounds.height as int)
    if (backgroundConfig.paint instanceof Paint) {
        g.paint = backgroundConfig.paint
        g.fillRect(bounds.x as int, bounds.y as int, bounds.width as int, bounds.height as int)
    }
    Image image
    if (backgroundConfig.node) {
        def node
        if (backgroundConfig.node instanceof GfxNode)
            node = backgroundConfig.node
        else if (backgroundConfig.node instanceof Closure)
            node = backgroundConfig.node.call(p)
        GfxContext context = new GfxContext()
        image = createCompatibleImage(bounds.width, bounds.height)
        context.g = image.graphics
        context.g.clip = g.clip
        context.component = p
        context.eventTargets = []
        context.groupSettings = [:]
        node.apply(context)
        context.g.dispose()
    } else if (backgroundConfig.image) {
        if (backgroundConfig.image instanceof Closure) {
            image = backgroundConfig.image.call(p)
        } else
            image = loadImage(backgroundConfig.image)
    }
    if (image) {
        def oldTransform = g.transform
        def oldComposite = g.composite
        if (backgroundConfig.composite instanceof Composite)
            g.composite = backgroundConfig.composite
        else if (backgroundConfig.composite instanceof Closure)
            g.composite = backgroundConfig.composite.call(p)
        if (backgroundConfig.transform instanceof AffineTransform)
            g.transform = backgroundConfig.transform
        else if (backgroundConfig.transform instanceof Closure)
            g.transform = backgroundConfig.transform.call(p)
        if (backgroundConfig.op instanceof BufferedImageOp)
            g.drawImage(image, backgroundConfig.op, 0, 0)
        else if (backgroundConfig.op instanceof Closure)
            g.drawImage(image, backgroundConfig.op.call(p), 0, 0)
        else
            g.drawImage(image, 0, 0, p)
        if (backgroundConfig.transform)
            g.transform = oldTransform
        if (backgroundConfig.composite)
            g.composite = oldComposite
    }
}

applyCss = { String css, component ->
    if (!css.toLowerCase().endsWith(".css"))
        css = css + ".css";
    if (getClass().classLoader.getResource(css))
        CSSDecorator.decorate(css, component)
}

templateBase = ''

decorateCss = { component ->
    applyCss("${templateBase}style", component)
    applyCss('style', component)
}

def stream
try {
    def cl = getClass().classLoader
    templateBase = "templates/${app.config.presentation.template}/"
    def resource = "${templateBase}template.groovy"
    stream = cl.getResourceAsStream(resource)
    def cls = new GroovyClassLoader(cl).parseClass(stream, resource)
    build(cls)
} catch (Exception e) {
    // ignore
} finally {
    if (stream)
        stream.close()
}

createHeader = { slide, page ->
    getMasterPart(slide, 'header', [page])
}

createFooter = { slide, page ->
    getMasterPart(slide, 'footer', [page])
}

