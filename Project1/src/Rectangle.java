import java.awt.Color;
public class Rectangle {
    double xUL;
    double yUL;
    double width;
    double height;
    Color col;
    public Rectangle(double x, double y, double w, double h){
        xUL = x;
        yUL = y;
        width = w;
        height = h;
    }
    public double getPerimeter(){
        double perimeter;
        perimeter = (2*width) + (2*height);
        return perimeter;
    }
    public double calculateArea(){
        double area;
        area = width * height;
        return area;
    }
    public void setColor(Color c){
        col = c;
    }
    public void setPos(double x, double y){
        xUL = x;
        yUL = y;
    }
    public void setHeight(double h){
        height = h;
    }
    public void setWidth(double w){
        width = w;
    }
    public Color getColor(){
        return col;
    }
    public double getXPos(){
        return xUL;
    }
    public double getYPos(){
        return yUL;
    }
    public double getHeight(){
        return height;
    }
    public double getWidth(){
        return width;
    }
}
