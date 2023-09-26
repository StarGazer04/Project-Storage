import java.awt.Color;
public class Triangle {
    double xBL;
    double yBL;
    double width;
    double height;
    Color col;
    public Triangle(double x, double y, double w, double h){
        xBL = x;
        yBL = y;
        width = w;
        height = h;
    }
    public double calculatePerimeter(){
        double perimeter;
        perimeter = ((2*Math.sqrt(width*width + height*height)) + width);
        return perimeter;
    }
    public double calculateArea(){
        double area;
        area = 0.5 * width * height;
        return area;
    }
    public void setPos(double x, double y){
        xBL = x;
        yBL = y;

    }
    public Color getColor(){
        return col;
    }
    public void setHeight(double h){
        height = h;
    }
    public void setWidth(double w){
        width = w;
    }
    public void setColor(Color c){
        col = c;
    }
    public double getXPos(){
        return xBL;
    }
    public double getYPos(){
        return yBL;
    }
    public double getHeight(){
        return height;
    }
    public double getWidth(){
        return width;
    }
}

