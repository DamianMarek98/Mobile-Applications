import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

public class MojeView extends View{

	private float[] dane=null;
	public void setDane(float[] dane) {
		this.dane = dane;
		j++;
	}

	private int i =0;
	private int j=0;
	
	public MojeView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		
		Paint p = new Paint();
		p.setARGB(255, 255, 0, 0);
		
		canvas.drawText("MojeView.onDraw: i="+i++, 200, 100, p);
		canvas.drawText("MojeView.onDraw: j="+j++, 200, 150, p);
		if(dane!=null)
		{
			canvas.drawLine(0, 0, 100*dane[0], 100*dane[0], p);
			p.setStyle(Style.STROKE);
			canvas.drawRect(100, 100, 200, 200, p);
			
		}
	}
	
	

}
