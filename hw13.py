#finishing touches that were made include:

    # 1.) changing intial starting position of my spacecraft within a limited y range, and changing the acceleration of both obsticals and my space craft. this was done beacuse i wanted the game to be a bit more slow after implementing the tracer method
    # 2.) right wall loops and left wall loops if spacecraft goes out of bounds
    # 3.) animated an explosion when spacecraft is hit or lands to hard
    # 4.) background with stars that is generated after player presses 'space' key.
    # 5.) preventing spacecraft to turn after it crashes or lands, and stops the counter and player input from showing up on terminal and turtle
    # 6.) made it so that if astroid hits bottom it loops to the top at random positions not near one another. 
    # 7.) print out player input for controls give, which is shown on bottom left of turtle screen. after each click on an arrow for movement, the previous string is cleared.
    # 8.) made a start screen where the space button needs to be pressed inorder to start the game. instructions are shown, and after player presses 'space', the ship, stars, and astoids are generated
    # 9.) removed astroid objcts after ship crashed so the screen looks better and the 'game over' string is more visable
    # 10.) created a moon object that the ship needs to land on. however its just a gray rectangle

import turtle
import random
import math
import time 

class Game:

    '''
Purpose: the main purpose of Game class is to use a gameloop to loop through animations that are inherited from other classes, as well as detect a crash through a method inside Game class.
         the game loop should stall until player presses 'start' and loop though animation until ship reaches a certain height or until it crashed into an object created within turtle from another class

Class variables: num_astroids1: number of astroids that appear on the top of the screen
                 num_astroids2: number of astroids that appear at the middle of the screen
                 start: string that will determin wether the gameloop loads the actual game or wether it stays on the start screen. this will be accesed through another class inorder to change it if player 
                        presses the 'space' key inorder for the player to begin playing the game. pressing the 'space' key will change the string to 'yes' and will begin running the game.
                counter_show_ship: counter value that is used to show the space ship. as long as counter is less than 1, the space craft will show. the counter value is used to ensure that multiple spacecrafts are not 
                                   shown on screen at a time when the game loops though each animation.

Instance variables: self.Loading_screen: inherits from Loading_screen class that generates the initial start screen and takes in instances within that class. 
                    self.player: inherits from SpaceCraft class that generates the spacecraft and takes in instance variables within the class that will be used in the gameloop to provide an animation of the objects movement

Methods: gameloop: used to loop though instances within other classes. purpose of the gameloop is to animate movement of the spacecraft, the falling astroids, and the animation of the crash every 30 ms.
         crash_test: determins wether or not the spacecraft crashes with an astroid object and is checked every 30ms
         moon: generates a landing spot colored grey to represent the moon

'''

    # class variables that refer to two sets of astroid groups: one that spawns at the top, and another that spawns at the middle to cause more chaos. using class vaiables allows easier access later on as well
    num_astroids1 = 10
    num_astroids2 = 10
    start = 'no'
    counter_show_ship = 0
    def __init__(self):
        self.start = 'no'
        #Bottom left corner of screen is (0, 0)
        #Top right corner is (500, 500)
        turtle.setworldcoordinates(0, 0, 500, 500)
        cv = turtle.getcanvas()
        cv.adjustScrolls()
        turtle.Screen().bgcolor('black')
        self.loading_screen = Loading_screen()
        turtle.tracer(0,0)
        self.moon()
        #Ensure turtle is running as fast as possible
        turtle.delay(0)
        self.player = SpaceCraft(random.uniform(100, 400), random.uniform(350, 370), random.uniform(-4,4), random.uniform(-1, 0))
        self.player.hideturtle()
        self.gameloop()
        turtle.onkeypress(self.loading_screen.begin_game, 'space')
        turtle.onkeypress(self.player.thrust, 'Up')
        turtle.onkeypress(self.player.turn_left, 'Left')
        turtle.onkeypress(self.player.turn_right, 'Right')
        #These two lines must always be at the BOTTOM of __init__
        turtle.listen()
        turtle.mainloop()

    def gameloop(self):
        # ensures start screen stays on unless class variable is not 'no'
        if Game.start == 'no':
            turtle.update()
            turtle.ontimer(self.gameloop, 30)
        else:
        # when y is above 20, run regural loop
            if self.player.start_y > 20:
                if Game.counter_show_ship < 1:
                    self.player.showturtle()
                self.crash_test()
                self.player.move()
                for i in self.loading_screen.astroids:
                        i.move_astroid()
                turtle.ontimer(self.gameloop, 30)  
                turtle.update()
            # if y is below 20 and x and y velocities are slow enough, player wins game, end game   
            elif self.player.start_y < 20 and (self.player.start_y_vel <= 3 and self.player.start_y_vel >= -3) or (self.player.start_x_vel <= 3 and self.player.start_x_vel >= -3):
                    # hides astoid objects
                    for i in self.loading_screen.astroids:
                        i.hideturtle()
                    turtle.penup()
                    turtle.speed(50)
                    turtle.goto(250, 0)
                    turtle.hideturtle()
                    turtle.clear()
                    turtle.color('chartreuse')
                    turtle.write("Succesful landing!!", align='center', font=(('Times New Roman'), 20))
                    self.player.hideturtle()
                    self.player.goto(0,0)
                    self.player.clear()
                    self.loading_screen.clear()

                    turtle.update()
                    turtle.done()
            # if y is below 20 and x and y velocities are too fast, player loses game, end game
            elif self.player.start_y < 20 and (self.player.start_y_vel > 3 or self.player.start_y_vel < -3) or (self.player.start_x_vel > 3 or self.player.start_x_vel < -3):
                    # tracer boolean value is no longer true, therefore updates to game are still on. i did this so that the animation for the crash would show up.
                    turtle.tracer(1)
                    # crash animation starts here___________
                    self.player.shapesize(20,20,20)
                    turtle.delay(100)
                    self.player.shape('circle')
                    self.player.color('orange')
                    turtle.delay(0)
                    self.player.hideturtle()
                    self.player.goto(0,0)
                    turtle.update()
                    # crash animation ends here ___________
                    turtle.penup()
                    turtle.speed(50)
                    turtle.goto(250, 0)
                    turtle.hideturtle()
                    turtle.color('deeppink')
                    turtle.write("rough landing!!", align='center', font=(('Times New Roman'), 20))
                    time.sleep(1)
                    turtle.clear()
                    #hides astroid objects
                    for i in self.loading_screen.astroids:
                        i.hideturtle()
                    turtle.speed(0)
                    turtle.goto(250, 0)
                    turtle.hideturtle()
                    turtle.color('red')
                    turtle.write("Game Over!", align='center', font=(('Times New Roman'), 20))
                    turtle.done()
                    
    # checks if spacecraft collided with astroid object
    def crash_test(self):
         SpaceCraft_x = self.player.start_x
         SpaceCraft_y = self.player.start_y
         for i in range(Game.num_astroids1 + Game.num_astroids2):
              astroid_x = self.loading_screen.astroids[i].start_x
              astroid_y = self.loading_screen.astroids[i].start_y
              total_distance = math.sqrt(((SpaceCraft_x - astroid_x)**2) + ((SpaceCraft_y - astroid_y)**2))
              if total_distance < 10:
                   turtle.tracer(1)
                   # crash animation #2 starts here _________
                   self.player.shapesize(15,15,15)
                   self.player.shape('circle')
                   turtle.delay(100)
                   self.player.color('orange')
                   turtle.delay(0)
                   self.player.hideturtle()
                   self.player.goto(0,0)
                   turtle.update()
                   # crash animation #2 ends here_________
                   turtle.penup()
                   turtle.speed(50)
                   turtle.goto(250, 0)
                   turtle.hideturtle()
                   turtle.color('deeppink')
                   turtle.write("You crashed!", align='center', font=(('Times New Roman'), 20))
                   time.sleep(1)
                   turtle.clear()
                   # hides astroid objects
                   for i in self.loading_screen.astroids:
                        i.hideturtle()
                   turtle.speed(0)
                   turtle.goto(250, 0)
                   turtle.hideturtle()
                   turtle.color('red')
                   turtle.write("Game Over!", align='center', font=(('Times New Roman'), 20))
                   turtle.done()

    # landing spot for the spacecraft
    def moon(self):
         b = turtle.Turtle()
         b.goto(-10,20)
         b.color('grey')
         b.begin_fill()
         b.goto(500,20)
         b.goto(500,0)
         b.goto(-10,0)
         b.goto(-10,20)
         b.end_fill()
         b.hideturtle()
    
class Loading_screen(turtle.Turtle):
    '''
Purpose: the main purpose of this class is to generate the inital start screen, as well as generate all background objects one 'space' key is presesed. Game class will inheit this entire class along with its methods
         to use in gameloop.
Class vaiables: counter: counter for space key to make sure it can only be pressed once to start the game
Instance variables: s: inherits from turtle
                    star_Xcord: empty list that will eventually contain x position of the stars for the background 
                    star_Ycord: empty list that will eventually contain y position of the stars for the background 
                    x_list: holds non reccuring x position of the stars
                    y_list: holds non reccuring y position of the stars
Methods: __init__: sets up starting screen
        begin_game: method starts once space key is pressed, which calls to the Game class
        star_sky: generates star backgound once teh space key is pressed
        create_astroid: generates the astroids once space key is pressed
'''
    # the loading screen. has instructions for the game and generates stars as well
    counter = 0
    def __init__(self):
            turtle.Turtle.__init__(self)
            self.speed(0)
            self.goto(250,435)
            self.color('grey')
            self.write("Welcome to Astroid Crash", align='center', font=(('Times New Roman'), 50))
            self.hideturtle()
            self.penup()
            self.goto(250,370)
            self.color('blue')
            self.write("Controls:", align='center', font=(('Times New Roman'), 20))
            self.hideturtle()
            self.penup()
            self.goto(250,350)
            self.color('blue')
            self.write("1.) left arrow: rotates ship left 15 degrees", align='center', font=(('Times New Roman'), 15))
            self.hideturtle()
            self.penup()
            self.goto(250,330)
            self.color('blue')
            self.write("2.) right arrow: rotates ship right 15 degrees", align='center', font=(('Times New Roman'), 15))
            self.hideturtle()
            self.penup()
            self.goto(250,310)
            self.color('blue')
            self.write("3.) up arrow moves ship up", align='center', font=(('Times New Roman'), 15))
            self.hideturtle()
            self.penup()
            self.goto(250,250)
            self.color('green')
            self.write("Objective: make it to the ground safely without crashing or landing too fast!", align='center', font=(('Times New Roman'), 15))
            self.hideturtle()
            self.penup()
            self.goto(250,100)
            self.color('grey')
            self.write('press space to start', align='center', font=(('Times New Roman'), 25))
            self.hideturtle()
            turtle.tracer(1)
            s = turtle.Turtle()
            s.color('white')
            s.speed(0)
            # star_Xcord stores x position values which will later be used with turtle.goto() function
            star_Xcord = []
            # star_Ycord stores y position values which will later be used with turtle.goto() function
            star_Ycord = []
            # x_list stores 
            x_list = []
            y_list = []
            for i in range(10):
                x = random.randint(0,500) 
                y = random.randint(140,210)
                while (x in x_list) or (y in y_list):
                    x = random.randint(0,500)
                    y = random.randint(140,210)
                star_Xcord.append(x)
                star_Ycord.append(y)
                for x in range(x-15, x+15):
                    if x in x_list:
                        x_list.remove(x)
                    x = [x]
                    x_list.extend(x)
                for y in range(y-2, y+2):
                    if y in y_list:
                        y_list.remove(y)
                    y = [y]
                    x_list.extend(x)
            for index in range(len(star_Xcord)): #y and x have same length, just using x in this case
                for k in range(5):
                        s.forward(5)
                        s.right(144)
                s.penup()
                s.goto(star_Xcord[index], star_Ycord[index])
                s.pendown()
                s.hideturtle()
            turtle.tracer(0)

    # if start is pressed change Game class variable to 'yes'. this allows for the actual game to run
    def begin_game(self):
        if Loading_screen.counter < 1:
             Game.start = 'yes'
             self.clear()
             self.Star_sky()
             self.create_astroid()
             Loading_screen.counter += 1
        # method used to generate background

    def Star_sky(self):
            s = turtle.Turtle()
            s.color('white')
            s.speed(0)
            # star_Xcord stores x position values which will later be used with turtle.goto() function
            star_Xcord = []
            # star_Ycord stores y position values which will later be used with turtle.goto() function
            star_Ycord = []
            # x_list stores 
            x_list = []
            y_list = []
            for i in range(100):
                x = random.randint(0,500)
                y = random.randint(25,500)
                while (x in x_list) or (y in y_list):
                    x = random.randint(0,500)
                    y = random.randint(25,500)
                star_Xcord.append(x)
                star_Ycord.append(y)
                for x in range(x-2, x+2):
                    if x in x_list:
                        x_list.remove(x)
                    x = [x]
                    x_list.extend(x)
                for y in range(y-2, y+2):
                    if y in y_list:
                        y_list.remove(y)
                    y = [y]
                    x_list.extend(x)
            for index in range(len(star_Xcord)): #y and x have same length, just using x in this case
                for k in range(5):
                        s.forward(5)
                        s.right(144)
                s.penup()
                s.goto(star_Xcord[index], star_Ycord[index])
                s.pendown()
                s.hideturtle()
                

    def create_astroid(self):
        # self.astroids will store all astroids 
        self.astroids = []
        # astroid_position1 will store all astroid x positions, along with values close to that. this list will be used to ensure no astroids spawn too close together at the top
        astroid_position1 = []
        # astroid_position2 will store all astroid x positions, along with values close to that. this list will be used to ensure no astroids spawn too close together at the middle
        astroid_position2 = []
        # loop through range of number of class Game variabel num_astroids
        for i in range(0, Game.num_astroids1):
            self.astroid = Astroids( 500, -0.5, random.uniform(0.5, 1))
            # while loop checks if x position in self.astroid is within astroid_position1
            while int(self.astroid.start_x) in astroid_position1:
                    self.astroid.start_x = random.uniform(5, 495)
            # if x position of self.astroid is not in list, then append it to self.astroids 
            self.astroids.append(self.astroid)
            # extend all values of the x position of range(self.astoid - 12), self.astroid + 12) to astroid_position1
            for num in range(int(self.astroid.start_x - 15), int(self.astroid.start_x + 15)):
                # helps shorthen list, for more efficency
                if num in astroid_position1:
                     astroid_position1.remove(num)
                num = [num]
                astroid_position1.extend(num)
        # loop through range of number of class Game variabel num_astroids
        for i in range(0, Game.num_astroids2):
            self.astroid = Astroids( 250, -0.5, random.uniform(0.5, 1))
             # while loop checks if x position in self.astroid is within astroid_position2
            while int(self.astroid.start_x) in astroid_position2:
                    self.astroid.start_x = random.uniform(5, 495)
            # if x position of self.astroid is not in list, then append it to self.astroids
            self.astroids.append(self.astroid)
             # extend all values of the x position of range(self.astoid - 12), self.astroid + 12) to astroid_position2
            for num in range(int(self.astroid.start_x - 15), int(self.astroid.start_x + 15)):
                # helps shorthen list, for more efficency
                if num in astroid_position2:
                     astroid_position2.remove(num)
                num = [num]
                astroid_position2.extend(num)

                       

class SpaceCraft(turtle.Turtle):
    '''
Purpose: this class represents information of the random position of the spacraft as soon as the game starts, along with movement animations due to gravity as well as turning animations.
Instance variables: self.start_x: x position of the space craft
                    self.start_y: y position of teh space craft
                    self.start_x_vel: x velocity of the spacecraft 
                    self.start_y_vel: y velocity of the spacecraft
                    self.fuel_remaining: amount of fuel left which represent number of arrows allowed to be pressed
Methods: __init__: creates instance variables for the position of the spacecraft along with its color
                   move: its position depending on x velocity and y velocity. moves spacecraft if its position is outside of the border defined in Game class
                   thrust: generates x and y velocities when the up arrow is pressed
                   turn_left: turns ship 15 degrees left when left arrow is pressed
                   turn_right: turns ship 15 degrees right when right arrow is pressed
'''

    def __init__(self, start_x = 0, start_y = 0, start_x_vel = 0, start_y_vel = 0):
        turtle.Turtle.__init__(self)
        self.color('red')
        self.start_x = start_x
        self.start_y = start_y
        self.start_x_vel = start_x_vel
        self.start_y_vel = start_y_vel
        self.fuel_remaining = 40
        self.left(90)
        self.penup()
        self.speed(0)
        self.goto(start_x, start_y)

    # moves spacecraft depending on its velcoity or its postion
    def move(self):
        # if spacecraft goes off screeen on the right side, teleprot it to the left side
        if self.start_x > 495:
              self.goto(5, self.start_y)
        # if spacecraft goes off screen on the left side, teleport it to the right side
        if self.start_x < 5:
             self.goto(495, self.start_y)
        self.start_y_vel -= 0.015
        self.start_x = self.xcor() + self.start_x_vel
        self.start_y = self.ycor() + self.start_y_vel
        self.goto(self.start_x, self.start_y)

    # movement when up arrow is pressed which is dependent on the angle of the spacecraft
    def thrust(self):
        if self.fuel_remaining > 0 and self.pos() > (0,0):
            turtle.clear()
            turtle.write('Up button pressed')
            self.fuel_remaining -= 1
            angle = math.radians(self.heading())
            cos_val = math.cos(angle)
            self.start_x_vel += cos_val
            sin_val = math.sin(angle)
            self.start_y_vel += sin_val
            print(self.fuel_remaining)
            if self.fuel_remaining == 0:
                turtle.clear()
                turtle.write('Out of fuel')

    # turns ship left 15 degrees if left arrow is pressed
    def turn_left(self):
        if self.fuel_remaining > 0 and self.pos() > (0,0):
            turtle.clear()
            turtle.write('Left button pressed')
            self.fuel_remaining -= 1
            self.left(15)
            print(self.fuel_remaining)
            if self.fuel_remaining == 0:
                turtle.clear()
                turtle.write('Out of fuel')
                
    # turns ship right when right 15 degrees when right arrow is pressed
    def turn_right(self):
        if self.fuel_remaining > 0 and self.pos() > (0,0):
            turtle.clear()
            turtle.write('Right button pressed')
            self.fuel_remaining -= 1
            self.right(15)
            print(self.fuel_remaining)
            if self.fuel_remaining == 0:
                turtle.clear()
                turtle.write('Out of fuel')
    

class Astroids(turtle.Turtle):

    '''
Purpose: generates astorid position and movement when the class is called
class variables: counter: represents number of astroids wanted to be generated which is defined in loading_screen class. used to generate new set of astroids 
                          everytime both top and middle astroids reach bottom of screen and need to be generated again
                 astroid_position_new: list which contains x postion of the astroids to ensure none are near eachother
Instance variables: self.start_y: y position of the astroid
                    self.start_x: x positon of the astroid
                    self.start_y_vel: start y velocity of the astroids. will be called onece astroids reach bottom of the screen so they have an inital y velocity 
                                      once they respawn at the top. this is used to make the animation a bit more clean looking.
                    self.start_x: random x position of the astroid
Methods: __inti__: sets up position of the astoid along with its color size
         move_astroid: moves astroid to the very top if they reach the lower end of the screen
         show_astoid: shows turtle object if called 
         hide_astroid: hides turtle object if called
'''
    # counter which will reset once all astoid are called 
    counter = 0
    # list which will contain x position of the astorids
    astroid_position_new = []

    def __init__(self, start_y = 0, start_y_vel = 0, size = 0):
            turtle.Turtle.__init__(self)
            self.start_y = start_y
            self.start_x = random.uniform(5, 495)
            self.start_y_vel = start_y_vel
            self.color('blue')
            self.shape('circle')
            self.shapesize(size, size, size)
            self.penup()
            self.speed(0)
            self.goto(random.uniform(5, 495), start_y)

    # moves astroid object to top of the screen if it reaches the bottom of the screen
    def move_astroid(self): 
        if self.start_y < 10:
            if self.counter == Game.num_astroids1 + Game.num_astroids2:
                Astroids.astroid_position_new = []
                Astroids.counter = 0
            self.start_y = 500
            self.start_x = random.uniform(5, 495)
            while int(self.start_x) in Astroids.astroid_position_new:
                    self.start_x = random.uniform(5, 495)
            for num in range(int(self.start_x - 15), int(self.start_x + 15)):
                        if num in Astroids.astroid_position_new:
                             Astroids.astroid_position_new.remove(num)
                        num = [num]
                        Astroids.astroid_position_new.extend(num)
            Astroids.counter += 1
            self.goto(self.start_x, self.start_y)
            self.start_y_vel = -1.5
        else:
            self.start_y_vel -= 0.095
            self.start_y = self.ycor() + self.start_y_vel
            self.goto(self.start_x, self.start_y)
    def show_astroid(self):
         self.hideturtle()
    def show_astroid(self):
         self.showturtle()



if __name__ == '__main__':
        Game()

    