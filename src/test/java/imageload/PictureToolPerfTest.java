package imageload;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.imaging.Imaging;
import org.imgscalr.Scalr;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.common.io.Files;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PictureToolPerfTest {
    private static final long TEST_DURATION = 10000; //Test duration in ms
    private static final long WARMUP_DURATION = 2000; //Warmup time in ms
    private static final long SLEEP_DURATION = 1000; //Sleep duration
    private static List<byte[]> _images;
    private static List<Properties> _properties;
    private static long testStart;
    private static int tests = 0;

    private static final String[] IMAGES = {
        "src/test/resources/picture/nikon_E990.jpg",
        "src/test/resources/picture/Nationalmuseum.jpg",
        "src/test/resources/picture/dice.png",
        "src/test/resources/picture/superman.gif",
        "src/test/resources/picture/Nationalmuseum.bmp"
    };

    @BeforeClass
    public static void loadImageData() throws Exception {
        _images = new ArrayList<>(IMAGES.length);
        _properties = new ArrayList<>(IMAGES.length);

        for (final String img : IMAGES) {
            _images.add(Files.toByteArray(new File(img)));
            final Properties props = new Properties();
            props.load(new FileInputStream(img + ".properties"));
            assertTrue(props.containsKey("picture.width"));
            assertTrue(props.containsKey("picture.height"));
            _properties.add(props);
        }

        final int count = countTestCases();
        final double time = (double) count * (double) (TEST_DURATION + WARMUP_DURATION + SLEEP_DURATION) / 1000.0;

        //This breaks if not all test cases use the sleep, warmup and test durations.
        System.out.println(String.format("Found %s test cases, should take about %.2f seconds.", count, time));

        testStart = System.currentTimeMillis();
    }

    @AfterClass
    public static void printTime() {
        final long duration = System.currentTimeMillis() - testStart;
        System.out.println(String.format("Performed %s tests in %.2f seconds.", tests, duration / 1000.0));
    }

    private static int countTestCases() {
        final Method[] methods = PictureToolPerfTest.class.getMethods();
        int count = 0;
        for (final Method m : methods) {
            final Annotation[] annotations = m.getDeclaredAnnotations();
            for (final Annotation a : annotations) {
                if (a.annotationType() == Test.class) {
                    count++;
                }
            }
        }

        return count;
    }

    @AfterClass
    public static void clearImages() {
        _images.clear();
    }

    @Test
    public void testInfoJavaImageIO() throws Exception {
        System.out.println("*** Info: ImageIO ***");

        final ImageInfoReader reader = new ImageInfoReader() {
            @Override
            public Dimension imageInfo(final byte[] buffer) throws Exception {
                final BufferedImage image = javax.imageio.ImageIO.read(new ByteArrayInputStream(buffer));
                return new Dimension(image.getWidth(), image.getHeight());
            }
        };

        testReadInfo(reader);
    }

    @Test
    public void testInfoApache() throws Exception {
        System.out.println("*** Info: Apache ***");

        final ImageInfoReader reader = new ImageInfoReader() {
            @Override
            public Dimension imageInfo(final byte[] buffer) throws Exception {
                return Imaging.getImageSize(buffer);
            }
        };

        testReadInfo(reader);
    }

    @Test
    public void testReadJavaImageIO() throws Exception {
        System.out.println("*** Read: ImageIO ***");
        final ImageReader reader = new ImageReader() {
            @Override
            public BufferedImage read(final byte[] buffer) throws Exception {
                return javax.imageio.ImageIO.read(new ByteArrayInputStream(buffer));
            }
        };
        testReader(reader);
    }

    @Test
    public void testReadApache() throws Exception {
        System.out.println("*** Read: Apache ***");
        final ImageReader reader = new ImageReader() {
            @Override
            public BufferedImage read(final byte[] buffer) throws Exception {
                return Imaging.getBufferedImage(buffer);
            }
        };
        testReader(reader);
    }

    @Test
    public void testResizeJava() throws Exception {
        System.out.println("*** Resize: Java ***");
        final ImageResize resize = new ImageResize() {
            @Override
            public BufferedImage resize(final BufferedImage input, final int width, final int height) {
                final BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
                final Graphics2D g = newImage.createGraphics();
                final AffineTransform scale = AffineTransform.getScaleInstance(width, height);
                g.drawRenderedImage(input, scale);

                return newImage;
            }
        };
        testResize(resize);
    }

    @Test
    public void testResizeImgScalrSpeed() throws Exception {
        System.out.println("*** Resize: ImgScalr/Speed ***");

        final ImageResize resize = new ImageResize() {
            @Override
            public BufferedImage resize(final BufferedImage input, final int width, final int height) {
                return Scalr.resize(input, Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT, width, height);
            }
        };
        testResize(resize);
    }

    @Test
    public void testResizeImgScalrSpeedAA() throws Exception {
        System.out.println("*** Resize: ImgScalr/Speed/AA ***");

        final ImageResize resize = new ImageResize() {
            @Override
            public BufferedImage resize(final BufferedImage input, final int width, final int height) {
                return Scalr.resize(input, Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT, width, height, Scalr.OP_ANTIALIAS);
            }
        };
        testResize(resize);
    }

    @Test
    public void testResizeImgScalrQuality() throws Exception {
        System.out.println("*** Resize: ImgScalr/Quality ***");

        final ImageResize resize = new ImageResize() {
            @Override
            public BufferedImage resize(final BufferedImage input, final int width, final int height) {
                return Scalr.resize(input, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, width, height);
            }
        };
        testResize(resize);
    }

    @Test
    public void testResizeImgScalrQualityHighest() throws Exception {
        System.out.println("*** Resize: ImgScalr/Quality-Highest ***");

        final ImageResize resize = new ImageResize() {
            @Override
            public BufferedImage resize(final BufferedImage input, final int width, final int height) {
                return Scalr.resize(input, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, width, height, Scalr.OP_ANTIALIAS);
            }
        };
        testResize(resize);
    }

    private void testReadInfo(final ImageInfoReader reader) throws Exception {
        tests++;
        Dimension dim = null;

        sleep();

        //First test for correct results.
        System.out.print("Sanity...");
        for (int i = 0; i < _images.size(); i++) {
            try {
                dim = reader.imageInfo(_images.get(i));
            }
            catch (final Exception e) {
                System.out.println("Error reading image: " + IMAGES[i]);
            }
            final Properties prop = _properties.get(i);

            assertEquals(Integer.parseInt(prop.getProperty("picture.width")), dim.width);
            assertEquals(Integer.parseInt(prop.getProperty("picture.height")), dim.height);
        }

        //Do a warmup
        System.out.print("Warmup...");
        long start = System.currentTimeMillis();
        long duration = 0;
        while (duration < WARMUP_DURATION) {
            for (int i = 0; i < _images.size(); i++) {
                dim = reader.imageInfo(_images.get(i));
            }
            duration = System.currentTimeMillis() - start;
        }

        //Actual test
        System.out.print("Performance...");
        long bytesRead = 0;
        start = System.currentTimeMillis();
        duration = 0;
        while (duration < TEST_DURATION) {
            for (int i = 0; i < _images.size(); i++) {
                final byte[] buffer = _images.get(i);
                dim = reader.imageInfo(_images.get(i));
                bytesRead += buffer.length;
            }
            duration = System.currentTimeMillis() - start;
        }

        outputDuration(duration, bytesRead);
    }

    /**
     * Tests reading a byte array with different image formats into a BufferedImage
     *
     * @param reader the reader
     * @throws Exception whenever stuff blows up
     */
    private void testReader(final ImageReader reader) throws Exception {
        tests++;
        BufferedImage image = null;

        sleep();

        //First test for correct results.
        System.out.print("Sanity...");
        for (int i = 0; i < _images.size(); i++) {
            try {
                image = reader.read(_images.get(i));
            }
            catch (final Exception e) {
                System.out.println("Error reading image: " + IMAGES[i]);
            }
            final Properties prop = _properties.get(i);

            assertEquals(Integer.parseInt(prop.getProperty("picture.width")), image.getWidth());
            assertEquals(Integer.parseInt(prop.getProperty("picture.height")), image.getHeight());
        }

        //Do a warmup
        System.out.print("Warmup...");
        long start = System.currentTimeMillis();
        long duration = 0;
        while (duration < WARMUP_DURATION) {
            for (int i = 0; i < _images.size(); i++) {
                image = reader.read(_images.get(i));
            }
            duration = System.currentTimeMillis() - start;
        }

        //Actual test
        System.out.print("Performance...");
        int bytesRead = 0;
        start = System.currentTimeMillis();
        duration = 0;
        while (duration < TEST_DURATION) {
            for (int i = 0; i < _images.size(); i++) {
                final byte[] buffer = _images.get(i);
                image = reader.read(_images.get(i));
                bytesRead += buffer.length;
            }
            duration = System.currentTimeMillis() - start;
        }

        outputDuration(duration, bytesRead);
    }

    private void testResize(final ImageResize resize) throws Exception {
        tests++;
        final int width = 320;
        final int height = 200;
        long bytes = 0;

        sleep();

        final ImageReader reader = new ImageReader() {
            @Override
            public BufferedImage read(final byte[] buffer) throws Exception {
                return Imaging.getBufferedImage(buffer);
            }
        };

        final List<BufferedImage> bufferedImages = new ArrayList<>(_images.size());

        for (final byte[] imgData : _images) {
            bytes += imgData.length;
            bufferedImages.add(reader.read(imgData));
        }

        System.out.print("Sanity...");
        for (final BufferedImage image : bufferedImages) {
            final BufferedImage newImage = resize.resize(image, width, height);
            assertEquals(width, newImage.getWidth());
            assertEquals(height, newImage.getHeight());
        }

        System.out.print("Warmup...");
        long start = System.currentTimeMillis();
        long duration = 0;
        while (duration < WARMUP_DURATION) {
            for (int i = 0; i < bufferedImages.size(); i++) {
                resize.resize(bufferedImages.get(i), width, height);
            }
            duration = System.currentTimeMillis() - start;
        }

        //Actual test
        System.out.print("Performance...");

        start = System.currentTimeMillis();
        duration = 0;
        int iterations = 0;
        while (duration < TEST_DURATION) {
            for (int i = 0; i < bufferedImages.size(); i++) {
                resize.resize(bufferedImages.get(i), width, height);
            }
            iterations++;
            duration = System.currentTimeMillis() - start;
        }
        outputDuration(duration, bytes * iterations);
    }

    private void sleep() {
        System.gc();
        try {
            Thread.sleep(SLEEP_DURATION);
        }
        catch (final InterruptedException e) {

        }
    }

    private void outputDuration(final long duration, final long bytesRead) {
        final double kbSec = (double) bytesRead / (double) duration * 1000.0 / 1048576.0;
        final double mbytes = bytesRead / 1048576.0;

        System.out.println(String.format("Processed %.2f MB in %.2f s (%.2f MB/s)", mbytes, duration / 1000.0, kbSec));
    }

    @Before
    public void setup() {

    }

    private interface ImageReader {
        BufferedImage read(byte[] buffer) throws Exception;
    }

    private interface ImageResize {
        BufferedImage resize(BufferedImage input, int width, int height);
    }

    private interface ImageInfoReader {
        Dimension imageInfo(byte[] buffer) throws Exception;
    }
}
