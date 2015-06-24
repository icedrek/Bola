package net.gbs.bola;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BolaVista extends SurfaceView implements SurfaceHolder.Callback{
	//Referencia al hilo usado para dibujar
	public BolaThread thread;
	//Variables para coordenadas de interaccion en la pantalla
	public int touched_x, touched_y, dibujoW, dibujoH;
	//Variable para indicar si se ha tocado la pantalla
	public boolean touched, movimiento;
	public Canvas canvas;
	//Constructor
	public BolaVista(Context context) {
		super(context);
		//Usaremos esta clase como manejador
		getHolder().addCallback(this);
	}

	//Llamada a este metodo si se produce algún cambio
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	//Llamada a este metodo al crear la superficie
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		//Llamada a nuestro hilo
		thread = new BolaThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();		
	}
	
	//Llamada a este metodo al destruir la superficie
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		Log.e("SurfaceDestroyed ", "Hilo detenido ");
		
		boolean retry = true;
		//El hilo se detiene
		while (retry){
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {}
		}
		
	}
	/*
	 * Dibujamos Circulo mediante Canvas
	 */
	@Override
	public void onDraw(Canvas canvas) {
		int ancho = canvas.getWidth();
		int alto = canvas.getHeight();
		this.canvas = canvas;
		
		//Mitad ancho
		int mitadW = ancho /2;
		dibujoW = mitadW;
		
		//Mitad alto
		int mitadH = alto /2;
		dibujoH = mitadH;
		
		//Fondo Blanco
		canvas.drawColor(Color.WHITE);
		
		//Se define pincel
		Paint pcirculo = new Paint();
		pcirculo.setColor(Color.BLACK);
		pcirculo.setStyle(Paint.Style.FILL);
		
		//dibujamos circulo usando el pincel en mitad de la pantalla
		if (movimiento){
			
			while (movimiento) {
				try{
					thread.sleep(10);
				} catch (InterruptedException e) {
					Log.e("InterruptedException ", "Movimiento bola ");
				}
				
				dibujoW++ ;
				dibujoH++;
				Log.e("valores X,Y ", dibujoW +","+dibujoH);
				canvas.drawCircle(dibujoW, dibujoH, 35, pcirculo);
			}
		} else {
			//Se dibuja en el centro
			canvas.drawCircle(mitadW, mitadH, 35, pcirculo);
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Utilizado al tocar la pantalla
		touched_x = (int) event.getX();
		touched_y = (int) event.getY();
		
		//Se lee accion
		int action = event.getAction();
		Log.e("touched (X,Y)", "(" + touched_x + "," + touched_y + ")");
		
		switch (action) {
		case MotionEvent.ACTION_DOWN: //Al tocar la pantalla
			Log.e("TouchEven ACTION_DOWN", "Se toca la pantalla ");
			touched = true;
			if (movimiento){
				movimiento = false;			
			}else{
				movimiento = true;
			}
				
			break;

		case MotionEvent.ACTION_MOVE: //Se desliza el dedo por la pantalla
			touched = true;
			Log.e("TouchEven ACTION_MOVE", "desplazamiento por pantalla ");			
			break;
			
		case MotionEvent.ACTION_UP: //Se deja de tocar la pantalla
			touched = false;
			Log.e("TouchEven ACTION_UP", "Se deja de tocar la pantalla ");			
			break;
			
		case MotionEvent.ACTION_CANCEL: //Se cancela?
			touched = false;
			Log.e("TouchEven ACTION_CANCEL", " ");			
			break;
			
		case MotionEvent.ACTION_OUTSIDE: //Se sale?
			touched = false;
			Log.e("TouchEven ACTION_OUTSIDE", " ");			
			break;

		default:
			break;
		}
		return true;
	}
	

}
