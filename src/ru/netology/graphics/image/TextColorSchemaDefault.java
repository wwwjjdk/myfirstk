package ru.netology.graphics.image;

public class TextColorSchemaDefault implements  TextColorSchema{
    char [] windows = new char[]{'#', '$', '@', '%', '*', '+', '-', '\''};
    char [] maxOs = new char[]{'▇', '●', '◉', '◍', '◎', '○', '☉', '◌', '-'};

    public TextColorSchemaDefault(){

    }
    @Override
    public char convert(int color) {
        return System.getProperty("os.name").startsWith("Windows")
                ?this.windows[(int)Math.floor((double) color/(256.0D/(double)this.windows.length))]
                :this.maxOs[(int)Math.floor((double) color/(256.0D/(double)this.maxOs.length))];
    }
}
