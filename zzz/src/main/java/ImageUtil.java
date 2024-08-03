import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    public static void main(String[] args) {
        String s = ImageUtil.imageToString("D:\\Work\\账号密码图片\\图片\\塔城小猫.png");
        ImageUtil.stringToImage(s, "D:\\塔城小猫.png");
        System.out.println(s);
    }

    /**
     * 将图片文件转换为字符串
     *
     * @param path 图片路径
     * @return String 图片转换的字符串
     */
    public static String imageToString(String path) {
        File imgFile = new File(path);
        ByteArrayOutputStream bos = null;
        try {
            // 获取图片格式
            String formatName = getFormatName(imgFile);
            // 读取图片文件
            BufferedImage bImage = ImageIO.read(imgFile);
            bos = new ByteArrayOutputStream();
            // 将BufferedImage转换为字节数组
            ImageIO.write(bImage, formatName, bos);
            byte[] imageBytes = bos.toByteArray();
            // 将字节数组转换为字符串
            return Base64.encodeBase64String(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("转换图片为字符串时发生错误", e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将字符串转换为图片文件，并保存到指定路径
     *
     * @param imageDataString 图片字符串
     * @param outputPath      图片的输出路径
     */
    public static void stringToImage(String imageDataString, String outputPath) {
        ByteArrayInputStream bis = null;
        try {
            // 将字符串转换为字节数组
            byte[] imageBytes = Base64.decodeBase64(imageDataString);
            bis = new ByteArrayInputStream(imageBytes);
            // 获取图片格式
            String formatName = getFormatName(new File(outputPath));
            // 将字节数组转换为BufferedImage
            BufferedImage bImage = ImageIO.read(bis);
            // 将BufferedImage写入文件
            ImageIO.write(bImage, formatName, new File(outputPath));
        } catch (IOException e) {
            throw new RuntimeException("转换字符串为图片时发生错误", e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取文件的格式名称
     *
     * @param file 文件
     * @return String 文件格式名称
     */
    private static String getFormatName(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0) {
            return "jpg"; // 使用默认格式
        }
        // 获取文件扩展名作为格式名称
        return fileName.substring(dotIndex + 1);
    }
}