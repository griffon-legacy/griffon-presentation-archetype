import java.awt.Color

master_Default = [
        // Set an instance of java.awt.Paint to paint the lowest background level
        // Paint may be a Paint or a Closure with one parameter (the Slide to paint to) returning a Paint
        paint: Color.WHITE,
        // Add an image (below) or a GfxBuilder node to paint ontop of the given Paint
        // Node may be a GfxBuilder node or a Closure with one parameter (the Slide to paint to) returning a GfxNode
        node: null,
        // The image can be set as String (resource loaded from the classpath), URL, File, InputStream or Image
        // Image may aswell be a Closure with one parameter (the Slide to paint to) returning an Image
        image: null,
        // java.awt.Composite used to draw the image
        // Composite may aswell be a Closure with one parameter (the Slide to paint to) returning a Composite
        composite: null,
        // java.awt.image.BufferedImageOp used to draw the image
        // Op may aswell be a Closure with one parameter (the Slide to paint to) returning a BufferedImageOp
        op: null,
        // java.awt.geom.AffineTransform used to draw the image
        // Transform may aswell be a Closure with one parameter (the Slide to paint to) returning a AffineTransform
        transform: null,
        // Component representing the header
        // Header may aswell be a Closure with two parameters (Slide slide, int page)
        // returning a Component
        header: null,
        // Component representing the footer
        // Footer may aswell be a Closure with two parameters (Slide slide, int page)
        // returning a Component
        footer: null
]

