// FractalDrawer class draws a fractal of a shape indicated by user input
import java.awt.Color;
import java.util.Scanner;
import java.util.ArrayList;

public class FractalDrawer {
    private double totalArea=0;  // member variable for tracking the total area

    public FractalDrawer() {}  // contructor


    //TODO:
    // drawFractal creates a new Canvas object
    // and determines which shapes to draw a fractal by calling appropriate helper function
    // drawFractal returns the area of the fractal
    public double drawFractal(String type) {
        Canvas drawing = new Canvas(3000,1000);
        // if statements used to call on helper function depending on what String type is
        if (type.equals("Circle")) {
            drawCircleFractal(140, 500, 450, Color.BLUE, drawing, 7);
        }
        else if(type.equals("Triangle")){
            drawTriangleFractal(240, 240, 450, 450, Color.PINK, drawing, 7);
        }
        else{
            drawRectangleFractal(300, 200, 350, 300, Color.BLACK, drawing, 7);
        }
        
        return totalArea;
    }


    //TODO:
    // drawTriangleFractal draws a triangle fractal using recursive techniques
    public void drawTriangleFractal(double width, double height, double x, double y, Color c, Canvas can, int level){
         if (0 < level) {
        Canvas drawing = can;
        Triangle myTriangle = new Triangle(x, y, width, height);
        totalArea += myTriangle.calculateArea();
        myTriangle.setColor(c);
        drawing.drawShape(myTriangle);
        // if statements used to change color of the shape objects through every recursion
        if (c.equals(Color.PINK)){
            c = Color.RED;
        }
        else if (c.equals(Color.RED)){
            c = Color.BLACK;
        }
        else{
            c = Color.PINK;
        }
        // three recursive calls for each side of the triangle object. each recursion should reduce the area of the shapes by 2 for every recursion. 
        drawTriangleFractal(width/2, height/2,  x + width/2 - width/4,  y + height/2, c,  can,  level - 1);
        drawTriangleFractal(width/2, height/2,  x + width/4 - width/2,  y - height/2 , c,  can,  level - 1);
        drawTriangleFractal(width/2, height/2,  x + 3 * width/4,  y - height/2 , c,  can,  level - 1);
    }
}


    // TODO:
    // drawCircleFractal draws a circle fractal using recursive techniques
    public void drawCircleFractal(double radius, double x, double y, Color c, Canvas can, int level) {
        if (0 < level) {
        Canvas drawing = can;
        Circle myCircle = new Circle(x,y,radius);
        totalArea += myCircle.caluclateArea();
        myCircle.setColor(c);
        drawing.drawShape(myCircle);
        // changes color through every recursion
        if (c.equals(Color.BLUE)){
            c = Color.RED;
        }
        else if (c.equals(Color.RED)){
            c = Color.YELLOW;
        }
        else{
            c = Color.BLUE;
        }
        // four recursions for top, bottom, and both sides of the circle shape
        drawCircleFractal(radius/2, x , y + radius + radius/2, c, drawing, level-1);
        drawCircleFractal(radius/2, x , y - radius - radius/2, c, drawing, level-1);
        drawCircleFractal(radius/2, x - radius - radius/2 , y , c, drawing, level-1);
        drawCircleFractal(radius/2, x + radius + radius/2, y, c, drawing, level-1);
        }
    }


    //TODO:
    // drawRectangleFractal draws a rectangle fractal using recursive techniques
    public void drawRectangleFractal(double width, double height, double x, double y, Color c, Canvas can, int level) {
        if (0 < level) {
        Canvas drawing = can;
        Rectangle myRectangle = new Rectangle(x, y, width, height);
        totalArea += myRectangle.calculateArea();
        myRectangle.setColor(c);
        drawing.drawShape(myRectangle);
        // changes color for every recursion, more colors than other colors
        if (c.equals(Color.BLACK)){
            c = Color.GRAY;
        }
        else if (c.equals(Color.GRAY)){
            c = Color.RED;
        }
        else if (c.equals(Color.RED)){
            c = Color.BLUE;
    }
        else{
            c=Color.BLACK;
        }
        // four recursions for each edge of the rectangle shape
        drawRectangleFractal(width/2,height/2, x - 0.5*width, y - 0.5 * height, c, can, level -1);
        drawRectangleFractal(width/2, height/2, x + width,  y - 0.5 * height, c,can, level - 1);
        drawRectangleFractal(width/2, height/2,  x + width,  y +  height,  c, can, level - 1);
        drawRectangleFractal(width/2,  height/2,  x - 0.5 * width,  y +  height, c, can, level - 1);
    }
}

    //TODO:
    // main should ask user for shape input, and then draw the corresponding fractal.
    // should print area of fractal
    public static void main(String[] args){
        // array list which individual String shapes can be added. 
        ArrayList<String> shapeList = new ArrayList<>();
        shapeList.add("Rectangle");
        shapeList.add("Triangle");
        shapeList.add("Circle");
        // calls for input to specify shape from the user
        Scanner input = new Scanner(System.in);
        System.out.print("Choose shape: Circle, Triangle, or Rectangle:");
        String shape = input.nextLine();
        // commands the user to type the correct shapes given through the list
        while(!shapeList.contains(shape)){
            // new input 
            
            System.out.print("wrong input,Choose shape: Circle, Triangle, or Rectangle:");
            String newShape = input.nextLine();
            shape = newShape;
        }
        // will call Fractal Drawer constructor depnding on what the user inputed as their desired shape
        if (shape.equals("Circle")) {
            FractalDrawer fractal = new FractalDrawer();
            fractal.drawFractal("Circle");
            System.out.print(fractal.drawFractal("Circle"));
        }
        else if (shape.equals("Rectangle")){
            FractalDrawer fractal = new FractalDrawer();
            System.out.print(fractal.drawFractal("Rectangle"));
        }
        else{
            FractalDrawer fractal = new FractalDrawer();
            System.out.print(fractal.drawFractal("Triangle"));
        }
        
        

    }
}

