package shared.parse.ui;

import android.content.Context;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;

final class ToastSaveCallback extends SaveCallback {
	public ToastSaveCallback(Context ctx) {
		this.ctx = ctx;
	}

	Context ctx;
	
	@Override
	public void done(ParseException e) {
		if (e == null) {
			Toast.makeText(ctx, "Save completed.", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(ctx, "Save error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}
	}
}