import java.awt.Color;
import java.awt.Graphics;

public class Plan {
	
	private KeyManager kM;
	private MouseManager mM;
	
	//put variables here
	Ray rayMouse=new Ray(0,0,true);
	Ray rayKey=new Ray(270,50,false);
	int[] mspd=new int[] {0,0};
	int macc=1,mmspd=6;//m for movement
	int tspd=0,tacc=1,mtspd=10;//t for turn
	double dis=rayKey.getMagnitude();
	boolean down=false;
	
	float[] mrLoc=new float[] {300,300},krLoc=new float[] {500,300};
	
	public Plan(KeyManager km,MouseManager mm) {
		this.kM=km;
		this.mM=mm;
	}
	
	public void update() {
		kM.update();
		//update variables here
		
		//rayMouse follows the mouse
		rayMouse.setDirection(Ray.findAngle(mrLoc[0], mrLoc[1], mM.getMX(), mM.getMY()));
		rayMouse.setMagnitude(Ray.findHypotenuse(mrLoc[0], mrLoc[1], mM.getMX(), mM.getMY()));
		//mouseRay motion
		if(mM.isM1()) {
			if(Ray.findHypotenuse(mrLoc[0], mrLoc[1], mM.getMX(), mM.getMY())<=macc) {
				mspd[0]=0;
				mrLoc=new float[] {mM.getMX(),mM.getMY()};
			}
			else if(Ray.findHypotenuse(mrLoc[0], mrLoc[1], mM.getMX(), mM.getMY())<mmspd) {
				if(mspd[0]>macc) {
					mspd[0]-=macc;
					if(mspd[0]<macc)mspd[0]=macc;
				}
			}
			else if(mspd[0]<mmspd) {
				mspd[0]+=macc;
				if(mspd[0]>mmspd)mspd[0]=mmspd;
			}
		}else {
				if(mspd[0]>0) {
					mspd[0]-=macc;
					if(mspd[0]<0)mspd[0]=0;
				}
		}
		//rayKey reacts to W,A,S,D
		if(kM.kA&&kM.kD || !kM.kA&&!kM.kD) {
			if(tspd>tacc)tspd-=tacc;
			else if(tspd<-tacc)tspd+=tacc;
			else tspd=0;
		}
		else if(kM.kA) {
			tspd-=tacc;
			if(tspd<-mtspd)tspd=-mtspd;
		}
		else if(kM.kD) {
			tspd+=tacc;
			if(tspd>mtspd)tspd=mtspd;
		}
		rayKey.setDirection(rayKey.getDirection()+tspd);
		
		dis=rayKey.getMagnitude();
		if(kM.kW) {
			dis+=tacc*3;
		}
		if(kM.kS) {
			dis-=tacc*3;
			if(dis<0)dis=0;
		}
		rayKey.setMagnitude(dis);
		
		if(kM.kSP) {
			if(mspd[1]<mmspd) {
				mspd[1]+=macc;
				if(mspd[1]>mmspd)mspd[1]=mmspd;
			}else {
				if(mspd[1]>0)mspd[1]-=macc;
				if(mspd[1]<0)mspd[1]=0;
			}
		}
		//keyRay motion
		if(kM.kSP) {
			if(mspd[1]<mmspd) {
				mspd[1]+=macc;
				if(mspd[1]>mmspd)mspd[1]=mmspd;
			}
		}else{
			if(mspd[1]>0) {
				mspd[1]-=macc;
				if(mspd[1]<0)mspd[1]=0;
			}
		}
		//moving the ray stations
		mrLoc[0]+=(new Ray(rayMouse.getDirection(),mspd[0],false).getX());
		mrLoc[1]+=(new Ray(rayMouse.getDirection(),mspd[0],false).getY());
		if(mrLoc[0]>800)mrLoc[0]=800;
		else if(mrLoc[0]<0)mrLoc[0]=0;
		if(mrLoc[1]>600)mrLoc[1]=600;
		else if(mrLoc[1]<0)mrLoc[1]=0;
		
		krLoc[0]+=(new Ray(rayKey.getDirection(),mspd[1],false).getX());
		krLoc[1]+=(new Ray(rayKey.getDirection(),mspd[1],false).getY());
		if(krLoc[0]>800)krLoc[0]=800;
		else if(krLoc[0]<0)krLoc[0]=0;
		if(krLoc[1]>600)krLoc[1]=600;
		else if(krLoc[1]<0)krLoc[1]=0;
	}
	public void render(Graphics g) {
		//draw here
		Color grn=new Color(8,180,130);
		Color blu=new Color(8,130,180);
		Color grnT=new Color(8,180,130,100);
		Color bluT=new Color(8,130,180,100);
		
		g.setColor(grn);
		g.drawOval((int)(mrLoc[0]-10), (int)(mrLoc[1]-15), 20, 30);
		g.fillArc((int)(mrLoc[0]-10), (int)(mrLoc[1]-15), 20, 20, 90, 90);
		g.drawLine((int)(mrLoc[0]-10), (int)(mrLoc[1]-5), (int)(mrLoc[0]+10), (int)(mrLoc[1]-5));
		g.setColor(grnT);
		g.fillOval((int)(mrLoc[0]-10), (int)(mrLoc[1]-15), 20, 30);
		
		g.setColor(blu);
		g.drawRect((int)(krLoc[0]-15), (int)(krLoc[1]-8), 30, 10);
		g.drawRect((int)(krLoc[0]-5), (int)(krLoc[1]-18), 10, 20);
		g.fillRect((int)(krLoc[0]-15), (int)(krLoc[1]+5), 31, 10);
		
		g.setColor(bluT);
		g.drawRect((int)(krLoc[0]-15), (int)(krLoc[1]-18), 30, 10);
		g.fillRect((int)(krLoc[0]-15), (int)(krLoc[1]-8), 30, 10);
		g.fillRect((int)(krLoc[0]-5), (int)(krLoc[1]-18), 10, 10);
		
		int radius=30;
		
		Ray tem=new Ray(rayMouse.getDirection(),radius*0.60,false);//create a temporary ray with the same direction, but magnitude radius
		tem.setMagnitude(-tem.getMagnitude());//flip the ray around
		g.setColor(new Color(30,0,0,150));
		g.fillRect((int)(mrLoc[0]+tem.getX())-11, (int)(mrLoc[1]+tem.getY())-6, 22, 12);
		g.setColor(new Color(150,150,150));
		g.drawString(Integer.toString((int)rayMouse.getDirection()), (int)(mrLoc[0]+tem.getX())-10, (int)(mrLoc[1]+tem.getY())+5);
		
		g.drawArc((int)(mrLoc[0]-radius), (int)(mrLoc[1]-radius), radius*2, radius*2, 0, -(int)rayMouse.getDirection());
		g.drawLine((int)(mrLoc[0]), (int)(mrLoc[1]), (int)(mrLoc[0]+radius), (int)(mrLoc[1]));
		
		g.setColor(new Color(10,250,180));
		g.drawLine((int)(mrLoc[0]), (int)(mrLoc[1]), (int)(mrLoc[0]+rayMouse.getX()), (int)(mrLoc[1]+rayMouse.getY()));
		
		g.drawString(Integer.toString((int)rayMouse.getMagnitude()), (int)(mrLoc[0]+(int)(rayMouse.getX()/2)-10), (int)(mrLoc[1]+(int)(rayMouse.getY()/2)));
		//-------------------------------------------------------------------------------------------------------------------------------------------------
		tem=new Ray(rayKey.getDirection(),radius*0.60,false);
		tem.setMagnitude(-tem.getMagnitude());
		g.setColor(new Color(30,0,0,150));
		g.fillRect((int)(krLoc[0]+tem.getX())-11, (int)(krLoc[1]+tem.getY())-6, 22, 12);
		g.setColor(new Color(150,150,150));
		g.drawString(Integer.toString((int)rayKey.getDirection()), (int)(krLoc[0]+tem.getX())-10, (int)(krLoc[1]+tem.getY())+5);
		
		g.drawLine((int)(krLoc[0]), (int)(krLoc[1]), (int)(krLoc[0]+radius), (int)(krLoc[1]));
		g.drawArc((int)(krLoc[0]-radius), (int)(krLoc[1]-radius), radius*2, radius*2, 0, -(int)rayKey.getDirection());
		
		g.setColor(new Color(10,180,250));
		g.drawLine((int)(krLoc[0]), (int)(krLoc[1]), (int)(krLoc[0]+rayKey.getX()), (int)(krLoc[1]+rayKey.getY()));
		
		g.drawString(Integer.toString((int)rayKey.getMagnitude()), (int)(krLoc[0]+(int)(rayKey.getX()/2)-10), (int)(krLoc[1]+(int)(rayKey.getY()/2)));
	}
}
