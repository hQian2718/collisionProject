import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashSet;

public class CollisionGUI implements ActionListener{
	SimulationPanel simulation;
	JFrame frame;
	JPanel toolBar;
	PauseButton pauser;
	int rectSpeed = 120;
	public CollisionGUI(){
		frame = new JFrame("Collisions");
		toolBar = new JPanel();
		
		//Initialize rectangles
		ArrayList<MovingRect> arr = new ArrayList<>();

		/*
		//random uniform rectangles demo

		for(int i = 0; i < 1000; i++){
			
			double vx = MovingRect.randI(-1 * rectSpeed, rectSpeed);
			double vy = Math.pow(rectSpeed * rectSpeed - vx * vx, 0.5) * (Math.random() > 0.5 ? 1 : -1);
			MovingRect newR = new MovingRect(i + 1 + "", MovingRect.randI(0, Constants.WORLDW),MovingRect.randI(0, Constants.WORLDH), 8, 8,vx, vy, 50);
			arr.add(newR);
		}
		*/
		
		
		//random size rectangles
		
		
		for(int i = 0; i < 30; i++){
			
			double vx = MovingRect.randI(-1 * rectSpeed, rectSpeed);
			double vy = Math.pow(rectSpeed * rectSpeed - vx * vx, 0.5) * (Math.random() > 0.5 ? 1 : -1);
			MovingRect newR = new MovingRect(i + 1 + "", MovingRect.randI(0, Constants.WORLDW),MovingRect.randI(0, Constants.WORLDH), MovingRect.randI(20, 40), MovingRect.randI(20, 40), vx, vy, -1);
			arr.add(newR);
		}
		
		
		/*
		
		//Gas diffusion demo
		for(int i = 0; i < 500; i++){
			double vx = MovingRect.randI(-1 * rectSpeed, rectSpeed);
			double vy = Math.pow(rectSpeed * rectSpeed - vx * vx, 0.5) * (Math.random() > 0.5 ? 1 : -1);
			MovingRect newR = new MovingRect(i + 1 + "", MovingRect.randI(0, Constants.WORLDW / 2),MovingRect.randI(0, Constants.WORLDH), 8, 8,vx, vy, 1, MovingRect.rectColors[1]);
			arr.add(newR);
		}
		rectSpeed = 40;
		for(int i = 501; i < 1000; i++){
			double vx = MovingRect.randI(-1 * rectSpeed, rectSpeed);
			double vy = Math.pow(rectSpeed * rectSpeed - vx * vx, 0.5) * (Math.random() > 0.5 ? 1 : -1);
			MovingRect newR = new MovingRect(i + 1 + "", MovingRect.randI(Constants.WORLDW / 2, Constants.WORLDW),MovingRect.randI(0, Constants.WORLDH), 8, 8,vx, vy, 1, MovingRect.rectColors[3]);
			arr.add(newR);
		}
		
		*/
		
		simulation = new SimulationPanel(arr);
		
		pauser = new PauseButton(simulation);
		StepButton stepper = new StepButton(simulation);
        toolBar.add(pauser);
        toolBar.add(stepper);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.add(simulation);
        frame.add(toolBar, BorderLayout.SOUTH);
        
        // Set the size and set the visibility
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
	}
	
	public CollisionGUI(ArrayList<MovingRect> arr){
		frame = new JFrame("Collisions");
		toolBar = new JPanel();
		
		simulation = new SimulationPanel(arr);
		
		pauser = new PauseButton(simulation);
		StepButton stepper = new StepButton(simulation);
        toolBar.add(pauser);
        toolBar.add(stepper);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.add(simulation);
        frame.add(toolBar, BorderLayout.SOUTH);
        
        // Set the size and set the visibility
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae){
	
	}
	
	public static void main(String[] args){
		 CollisionGUI col = new CollisionGUI();
	}
	
}

class SimulationPanel extends JPanel implements ActionListener{

	private static final Color[] colorOf = {new Color(230, 230, 230), new Color(30, 30, 30), new Color(230, 60, 60)};
	private static final int BACKGROUND = 0;
	private static final int BOX = 1;
	private static final int COLLIDE = 2;
	
	public Timer timer = new Timer(1000 / Constants.FPS, this);
	
	private int frameReference;
	CollisionChecker cc;
    
	public SimulationPanel(){
		super();
		this.cc = new CollisionChecker();
		this.frameReference = 0;
		this.timer.start();
		repaint();
	}
	
	public SimulationPanel(ArrayList<MovingRect> arr){
		super();
		this.cc = new CollisionChecker(arr);
		this.frameReference = 0;
		this.timer.start();
		repaint();
	}
	
	public void addRandomRects(int amount){
		for(int i = 0; i < amount; i++){
			this.cc.objects.add(MovingRect.randomRect("" + i));
		}
	}
	
	@Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.WORLDW, Constants.WORLDH);
    }
	
	public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	g.setColor(colorOf[BACKGROUND]);
    	g.fillRect(0, 0, this.getWidth(), this.getHeight());
    	frameReference++;
    	//System.out.println(frameReference);
    	g.setColor(colorOf[BOX]);
    	g.drawString(frameReference + "", 10, 10);
    	
    	for(MovingRect mr: cc.objects){
    		int[] params = mr.drawingParams();
    		g.setColor(mr.c);
    		//if(CollisionChecker.hasCollision(mr, cc.objects.get(0)))	g.setColor(colorOf[COLLIDE]);
    		g.fillRect(params[0], params[1], params[2], params[3]);
    		g.drawString(mr.id + "\n" + cc.qt.getNode(mr).toString(), params[0]+ 10, params[1] + 10);
    		
    	}
    	
    	g.setColor(Color.RED);
    	drawQuadNode(cc.qt.root, g);
    	
    	
	}
	
	public void drawQuadNode(QuadNode n, Graphics g){
		g.drawRect((int) n.x, (int) n.y, (int) n.width, (int) n.height);
		if(!n.isFinal){
			for(QuadNode child : n.nodes){
				drawQuadNode(child, g);
			}
		}
	}

 	public void actionPerformed(ActionEvent ev){
    	if(ev.getSource()==timer){
    		this.cc.update();
      		repaint();
      	}
    }
}

class PauseButton extends JButton implements ActionListener{
	SimulationPanel sim;
	
	public PauseButton(SimulationPanel sim){
		super("pause");
		this.sim = sim;
		this.addActionListener(this);
	}
	
 	public void actionPerformed(ActionEvent ev){
    	if(this.sim.timer.isRunning()) 	this.sim.timer.stop();
    	else							this.sim.timer.start();
    }
}

class StepButton extends JButton implements ActionListener{
	SimulationPanel sim;
	
	public StepButton(SimulationPanel sim){
		super("step");
		this.sim = sim;
		this.addActionListener(this);
	}
	
 	public void actionPerformed(ActionEvent ev){
    	this.sim.cc.update();
    	this.sim.repaint();
    }
}
interface PhysicsShape{

}

class MovingRect extends Rectangle2D.Double implements PhysicsShape{
	String id;
	double vx;
	double vy;
	double mass;
	Color c;
	static final Color[] rectColors = {new Color(105,78,130),
												new Color(236,134,206),
												new Color(255,205,55),
												new Color(61,133,198),
												new Color(219,11,133),
												};
	
	public MovingRect(){
		this("Rect", 0, 0, 0, 0, 0, 0, 1);
	}
	
		public MovingRect(double x, double y, double width, double height, double vx, double vy, double m){
		this("Rect", x, y, width, height, vx, vy, m);
	}
	
	public MovingRect(String id, double x, double y, double width, double height, double vx, double vy, double m){
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.vx = vx;
		this.vy = vy;
		if(mass < 0)	this.mass = this.width * this.height / 100;
		this.mass = m;
		//System.out.println(this.mass);
		this.c = rectColors[randI(0, rectColors.length - 1)];
	}
	
	public MovingRect(String id, double x, double y, double width, double height, double vx, double vy, double m, Color color){
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.vx = vx;
		this.vy = vy;
		if(mass < 0)	this.mass = this.width * this.height / 100;
		this.mass = m;
		//System.out.println(this.mass);
		this.c = color;
	}
	
	public MovingRect nextRect(){
		MovingRect mr = (MovingRect) this.clone();
		mr.step();
		return mr;
	}
	
	public static int randI(int low, int high){
		return low + (int) (Math.random() * (high - low + 1));
	}
	
	public static MovingRect randomRect(){
		return new MovingRect("Rect", randI(0, Constants.WORLDW), randI(0, Constants.WORLDH), randI(20, 50), randI(20, 50), randI(-30, 30), randI(-30, 30), 10);
	}
	
	public static MovingRect randomRect(String id){
		return new MovingRect(id, randI(0, Constants.WORLDW), randI(0, Constants.WORLDH), randI(10, 50), randI(10, 50), randI(-90, 90), randI(-90, 90), 10);
	}
	
	public double speed(){
		return Math.pow(vx*vx+vy*vy, 0.5);
	}
	
	public double right(){
		return this.x + this.width;
	}
	
	public double bottom(){
		return this.y + this.height;
	}
	
	public double nextX(){
		return this.x + vx;
	}
	
	public double nextY(){
		return this.y + vy;
	}
	
	public void step(){
		
		this.x += vx / Constants.FPS;
		this.y += vy / Constants.FPS;
		
		if(this.x < 0 || this.right() > Constants.WORLDW){
			this.vx = -vx;
			if(this.x < 0)		this.x -= 2*x;
			else 				 this.x = 2*(Constants.WORLDW - this.width) - x;
		}
		
		if(this.y < 0 || this.bottom() > Constants.WORLDH){
			this.vy = -vy;
			if(this.y < 0)  this.y -= 2*y;
			else this.y = 2*(Constants.WORLDH - this.height) - y;
		}
	}
	
	public void step(double dt){
		this.x += vx * dt;
		this.y += vy * dt;
		if(this.x < 0 || this.right() > Constants.WORLDW){
			this.vx = -vx;
			if(this.x < 0)		this.x -= 2*x;
			else 				 this.x = 2*(Constants.WORLDW - this.width) - x;
		}
		
		if(this.y < 0 || this.bottom() > Constants.WORLDH){
			this.vy = -vy;
			if(this.y < 0)  this.y -= 2*y;
			else this.y = 2*(Constants.WORLDH - this.height) - y;
		}
	}
	
	//return integer estimates for positions for drawing.
	public int[] drawingParams(){
		int[] params = { (int) x, (int) y,(int) width, (int) height};
		return params;
	}
	
	public String toString(){
		return ("Size: " + width + " x " + height + ". Pos: " + x + ", " + y + "; v: <" + vx + ", " + vy + ">.");
	}
}

class CollisionChecker{
	public ArrayList<MovingRect> objects;
	public QuadTree qt;
	private HashSet<QuadNode> visited;
	private int frameNumber;
	
	public CollisionChecker(){
		this.objects = new ArrayList<MovingRect>();
		this.qt = new QuadTree(Constants.WORLDW, Constants.WORLDH);
		this.visited = new HashSet<>();
		this.frameNumber = 1;
		for(MovingRect mr : objects){
			qt.root.insert(mr);
		}
		qt.clear();
		for(MovingRect mr : objects){
			qt.root.insert(mr);
		}
	}
	
	public CollisionChecker(ArrayList<MovingRect> arr){
		this.objects = arr;
		this.qt = new QuadTree(Constants.WORLDW, Constants.WORLDH);
		this.visited = new HashSet<>();
		this.frameNumber = 0;
		for(MovingRect mr : objects){
			qt.root.insert(mr);
		}
		
	}
	public static int randI(int low, int high){
		return low + (int) (Math.random() * (high - low + 1));
	}
	
	//gets called every frame. 
	public void update(){
		visited.clear();
		qt.clear();
		for(MovingRect mr: objects){
			mr.step();
			qt.insert(mr);
		}
		
		//System.out.println("\nChecking Collisions on frame " + this.frameNumber++);
		for(MovingRect mr : objects){
			QuadNode qn = qt.getNode(mr);
			if(!visited.contains(qn)){
				//System.out.println("\tin quadNode " + qn.toString());
				for(int i = 1; i < qn.currChild; i++){
					for(int j = 0; j < i; j++){
						//System.out.println("\t\tchecking " + qn.children[j].id + " against " + qn.children[i].id);
						if(hasCollision(qn.children[j], qn.children[i]))	resolveCollision(qn.children[j], qn.children[i]);
						}
					}
					
				visited.add(qn);
			}
		}
		/*
		for(int i = 1; i < objects.size(); i++){
			for(int j = 0; j < i; j++){
				if(hasCollision(objects.get(i), objects.get(j))){
					resolveCollision(objects.get(i), objects.get(j));
				}
			}
		}
		
		*/
		if(frameNumber > 10000)	frameNumber = 1;
	}
	
	public static boolean hasCollision(MovingRect r1, MovingRect r2){
		if(r1 == null || r2 == null)	return false;
		if(r1.equals(r2))	return false;
		if(((r1.x - r2.right()) >= 0 ^ (r1.right() - r2.x) >= 0) && (r1.y - r2.bottom() >= 0) ^ (r1.bottom() - r2.y >= 0)){
			return true;
		}
		return false;
	}
	
	//returns the time passed since collision occurred.
	public double collisionTime(MovingRect r1, MovingRect r2){
		//set reference to r2. Calculate relative velocity of r1
		if(!hasCollision(r1, r2))	return -1.0;
		
		double vrx = -r2.vx + r1.vx;
		double vry = -r2.vy + r1.vy;
		
		double xOverlap, yOverlap;
		if(vrx > 0)	xOverlap = r1.right() - r2.x;
		else		xOverlap = r1.x - r2.right();
		if(vry > 0) yOverlap = r1.bottom() - r2.y;
		else		yOverlap = r1.y - r2.bottom();
		
		double xTime = xOverlap / vrx;
		double yTime = yOverlap / vry;
		
		double result = 10.0;
		if(xTime > Constants.MIN_DELTA)	result = xTime;
		if(yTime > Constants.MIN_DELTA) result = Math.min(result, yTime);
		
		if(result == 10.0)	return -1.0;
		else	return result;
	}
	
	public void resolveCollision(MovingRect r1, MovingRect r2){
		double timeSince = collisionTime(r1, r2);
		//System.out.print(timeSince + " sec. since " + r1.id + " collided with " + r2.id + "\nInitial ");
		
		if(timeSince < Constants.MIN_DELTA)	return;
		r1.step(-timeSince);
		r2.step(-timeSince);
		//printKE(r1, r2);
		//System.out.println(r1.mass + ", " + r2.mass);
		double totMass = r1.mass + r2.mass;
		double dMass = r1.mass - r2.mass;
		double u = dMass * 1.0 / totMass;
		
		//System.out.println(totMass + ", " + dMass + ", " + u);
		double result1, result2;
		
		//System.out.println("vx1 = " + r1.vx + ", vx2= " + r2.vx);
		result1 = u * r1.vx + (2 * r2.mass/totMass) * r2.vx;
		result2 = (2 * r1.mass/totMass) * r1.vx - u * r2.vx;
		//System.out.println("vx1 = " + result1 + ", vx2= " + result2);
		r1.vx = result1;
		r2.vx = result2;
		
		result1 = u * r1.vy + (2 * r2.mass/totMass) * r2.vy;
		result2 = (2 * r1.mass/totMass) * r1.vy - u * r2.vy;
		
		r1.vy = result1;
		r2.vy = result2;
		
		r1.step(timeSince);
		r2.step(timeSince);	
		//printKE(r1, r2);
	}
	
	public void printKE(MovingRect r1, MovingRect r2){
		System.out.println("KE: " + r1.mass * r1.speed() * r1.speed() / 2 + " + " +  r2.mass * r2.speed() * r2.speed() / 2);
	}
}

class QuadTree{
	public static final int MAX_CHILD = 4;
	public int w, h;
	
	public QuadNode root;
	public QuadTree(int w, int h){
		this.w = w;
		this.h = h;
		this.root = new QuadNode(0, 0, w, h, MAX_CHILD, 5, 0, 0, null);
	}
	public void insert(MovingRect mr){
		this.root.insert(mr);
	}
	
	public void clear(){
		this.root.clear();
	}
	
	public QuadNode getNode(MovingRect r){
		QuadNode curr = root;
		while(!curr.isFinal){
			curr = curr.nodes[curr.findIndex(r)];
		}
		return curr;
	}
	
	
	
	
}

class QuadNode extends Rectangle2D.Double{
	public static final int TL = 0;
	public static final int TR = 1;
	public static final int BL = 2;
	public static final int BR = 3;
	
	protected boolean isFinal = true;
	protected int max_child, max_depth, depth, currChild;
	protected int dirID;
	
	public QuadNode parent;
	
	QuadNode[] nodes;
	MovingRect[] children;
	ArrayList<MovingRect> stuckChildren;
	
	public QuadNode(double x, double y, double w, double h, int max_child, int max_depth, int depth, int dirID, QuadNode p){
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.max_child = max_child;
		this.max_depth = max_depth;
		this.depth = depth;
		this.dirID = dirID;
		this.parent = p;
		
		currChild = 0;
		nodes = new QuadNode[4];
		children = new MovingRect[max_child];
		stuckChildren = new ArrayList<>();
	}
	
	public boolean containsRect(MovingRect r){
		if(r.right() < this.x || r.x > this.x + this.width){
			if(r.y > this.y + this.height || r.bottom() < this.y){
				return false;
			}
		}
		return true;
	}
	
	public boolean coversRect(MovingRect r){
		if(r.x > this.x && r.right() < this.x + this.width && r.y > this.y && r.bottom() < this.y + this.height){
			return true;
		}
		return false;
	}
	
	public void splitFour(){
		this.isFinal = false;
		double neww = width / 2;
		double newh = height / 2;
		this.nodes[TL] = new QuadNode(x, y, neww, newh, max_child, max_depth, depth + 1, TL, this);
		this.nodes[TR] = new QuadNode(x + neww, y, neww, newh, max_child, max_depth, depth + 1, TR, this);
		this.nodes[BL] = new QuadNode(x, y + newh, neww, newh, max_child, max_depth, depth + 1, BL, this);
		this.nodes[BR] = new QuadNode(x + neww, y + newh, neww, newh, max_child, max_depth, depth + 1, BR, this);
	}
	
	public void insert(MovingRect r){
		if(!this.isFinal){
			QuadNode nextTry= this.nodes[findIndex(r)];
			//if(nextTry.coversRect(r)) nextTry.insert(r);
			//else{
			//	this.stuckChildren.add(r);
			//	return;
			//}
		}else if(this.currChild < max_child){
			children[currChild++] = r;
		}else{
			this.splitFour();
			for(MovingRect rc: children){
				this.insert(rc);
			}
			this.insert(r);
			this.currChild = 0;
			this.isFinal = false;
		}
	}
	
	//how to deal with collisions on the borders?
	
	public void clear(){
		this.currChild = 0;
		if(!this.isFinal){
			for(QuadNode child : nodes){
				child.clear();
			}
			this.isFinal = true;
		}
	}
	
	public int findIndex(MovingRect r){
		int index = 0;
		if(r.x > x + width / 2)	index++;
		if(r.y > y + height / 2) index += 2;
		return index;
	}
	
	public int findIndex(QuadNode r){
		int index = 0;
		if(r.x >= x + width / 2 - 1)	index++;
		if(r.y >= y + height / 2 - 1) index += 2;
		return index;
	}
	
	public String toString(){
		return("(" + this.x + ", " + this.y + ") lv." + this.depth);
	}
	
	
	
}


class Constants{
	public static final int FPS = 24;
	public static final double SPF = 1.0 / FPS;
	public static final double MIN_DELTA = 0.00001;
	public static final double MASS_THRESHOLD = 10000;
	public static final int WORLDW = 1100;
	public static final int WORLDH = 700;
}











/*
	GUI
	SimulationPanel --> where drawing happens. Calls checkCollision(int timeElapsed);
	
	CollisionChecker --> where collision logic is checked. Contains objects
	
	MovingRect --> stores position, velocity, mass, and size data
	



*/