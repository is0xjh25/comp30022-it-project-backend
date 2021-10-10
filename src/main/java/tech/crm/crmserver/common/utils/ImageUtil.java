package tech.crm.crmserver.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import tech.crm.crmserver.common.constants.ImageConstant;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class to compress image
 * See https://cloud.tencent.com/developer/article/1667686
 */
public class ImageUtil {
    /**
     * compress to 256*256
     * @param source source
     * @param output output
     * @throws IOException
     */
    public static void imgThumb(InputStream source, OutputStream output) throws IOException {
        Thumbnails.of(source).size(ImageConstant.WIDTH, ImageConstant.HEIGHT).toOutputStream(output);
    }

    /**
     * compress by scale
     * @param source source
     * @param output output
     * @param scale  scale
     * @throws IOException
     */
    public static void imgScale(InputStream source, OutputStream output, double scale) throws IOException {
        Thumbnails.of(source).scale(scale).toOutputStream(output);
    }
}
