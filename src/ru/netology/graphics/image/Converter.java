package ru.netology.graphics.image;

import java.io.IOException;

import  java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import  java.awt.image.WritableRaster;

import java.net.URL;
import  javax.imageio.ImageIO;

public class Converter implements  TextGraphicsConverter{

    private int maxWidth = 200;
    private int maxHeight = 400;
    private double maxRatio;//весь размер (дио)
    private TextColorSchema schema;

 public Converter(){

 }
    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        if(!checkRatio(img)){
            throw new BadImageSizeException(Math.abs(img.getWidth()/ img.getHeight()), this.maxRatio);
        }else{
            if(schema == null){
                schema = new TextColorSchemaDefault();
            }
        }

        double scale = calcScale(img.getHeight(), img.getWidth());
        int newHeight = (int)((double)img.getHeight()/ scale);
        int newWidth = (int)((double)img.getWidth()/ scale);
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, 4);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, 10);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, (ImageObserver) null);
        WritableRaster bwRaster = bwImg.getRaster();// для прохода по пикселям
        StringBuilder picture = new StringBuilder();
        for(int h = 0; h < bwRaster.getHeight(); h++){
            for(int w = 0; w < bwRaster.getWidth(); w ++){
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                picture
                        .append(c)
                        .append(c);
            }
            picture.append("\n");
        }
        return picture.toString();
    }

    private boolean checkRatio(BufferedImage img){
        if(this.maxRatio != 0.0D){
            double trueRatio = Math.abs(img.getWidth()/ img.getHeight());
            return  !(maxRatio < trueRatio);
        }else{
            return true;
        }
    }

    private double calcScale(int height , int width){
        if((maxWidth >= width || maxWidth == 0) && (maxHeight >= height || maxHeight == 0)){
            return  1.0D;
        }else{
            double a =  (double)height / (double)maxHeight;
            double b = (double) width / (double) maxWidth;
            return Math.max( a, b);
        }
    }

    @Override
    public void setMaxWidth(int width) {
     this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
         this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
     this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema x) {
      schema = x;
    }
}
