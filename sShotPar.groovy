@Grab(group='org.xhtmlrenderer', module='flying-saucer-core', version='9.0.7')
@Grab(group='jtidy', module='jtidy', version='4aug2000r7-dev')
@Grab(group='org.jsoup', module='jsoup', version='1.7.2')

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import groovyx.gpars.GParsPool
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import org.w3c.dom.Document
import org.w3c.tidy.Tidy
import org.xhtmlrenderer.simple.Graphics2DRenderer

//Uncomment the following line to send traffic through Tor.
//System.properties.putAll( ["socksProxyHost":"localhost", "socksProxyPort":"9050"] )

def makeThumbnail(address) {
	//Set the running directory
	String runningDirectory = new File(getClass().protectionDomain.codeSource.location.path).parent
	// Size for the renderer
	def WIDTH = 1600
	def HEIGHT = 900
	// Setup Tidy
	def tidy = new Tidy()
	tidy.with {
		setQuiet(true)
		setXHTML(true)
		setShowWarnings(false)
	}
	def url = new URL(address)
	def doc = tidy.parseDOM(new ByteArrayInputStream(url.text.getBytes("UTF-8")), null)
	def os = new FileOutputStream("${runningDirectory}/images/${url.getHost()}.png")
	def buf = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)
	def graphics = buf.createGraphics()
	def renderer = new Graphics2DRenderer()
	renderer.with {
		setDocument(doc, address)
		layout(graphics, new Dimension(WIDTH, HEIGHT))
		render(graphics)
		graphics.dispose()
		ImageIO.write(buf, "png", os)
	}
}


makeThumbnail(args[0])
