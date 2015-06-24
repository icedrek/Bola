package net.gbs.bola;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class BolaThread extends Thread {
	private SurfaceHolder sh;
	private BolaVista vista;
	private boolean run;
	
	/*
	 * El constructor de nuestra vista recibe como parametros:
	 *   - SurfaceHolder
	 *   - nuestro View (BolaVista)
	 */
	public BolaThread(SurfaceHolder sh, BolaVista vista) {
		this.sh = sh;
		this.vista = vista;
		run = false;
	}
	
	//Metodo que indica si el hilo está en ejecución o no
	public void setRunning(boolean run){
		this.run = run;
	}
	
	public void run() {
		//Instancia a canvas
		Canvas canvas;
		
		//Pinta mientras la variable run sea true
		while(run){			
			canvas = null;
			
			try {
				//Area a pintar = null
				canvas = sh.lockCanvas(null);
				//Se usa synchronized para asegurar que no se usa el objeto en otro hilo
				synchronized(sh) {
					//Se ejecuta el metodo onDraw y el canvas para dibujar en surface vista
					vista.onDraw(canvas);
				}
			} finally {
				//En caso de error se libera el canvas para no dejar el surfaceview inconsistente
				if (canvas != null)
					//Se libera el canvas
					sh.unlockCanvasAndPost(canvas);
			}
		}
	}
}
