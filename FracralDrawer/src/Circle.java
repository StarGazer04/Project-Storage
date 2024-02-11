import java.awt.Color;
public class Circle {
    private double xPos;
    private double yPos;
   private double radius;
    private Color col;
    public Circle(double x, double y, double r){
        xPos = x;
        yPos = y;
        radius = r;
    }
    public double calculatePerimeter(){
        double perimeter;
        perimeter = 2 * Math.PI * radius;
        return perimeter;
    }
    public double caluclateArea(){
        double area;
        area = Math.PI * radius * radius;
        return area;
    }
    public void setColor(Color color){
        col = color;
    }
    public void setPos(double x, double y){
        xPos = x;
        yPos = y;
    }
    public void setRadius(double r){
        radius = r;
    }
    public Color getColor(){
        return col;
    }
    public double getXPos(){
        return xPos;
    }
    public double getYPos(){
        return yPos;
    }
    public double getRadius(){
        return radius;
    }
}
