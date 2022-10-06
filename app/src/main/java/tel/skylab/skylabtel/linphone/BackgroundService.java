package tel.skylab.skylabtel.linphone;

import static tel.skylab.skylabtel.linphone.fragments.StatusBarFragment.mStatusText;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class BackgroundService extends Service {
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();

        handler = new Handler();
        runnable =
                new Runnable() {
                    public void run() {
                        if (mStatusText
                                .getText()
                                .toString()
                                .trim()
                                .equalsIgnoreCase("Not connected")) {
                            Toast.makeText(
                                            context,
                                            "Please Wait Server Connect With Your Application.",
                                            Toast.LENGTH_LONG)
                                    .show();
                            LinphoneManager.getInstance().changeStatusToOnline();
                        } else {
                            onDestroy();
                            stopSelf();
                        }
                        /*startActivity(
                        new Intent(getApplicationContext(), DashboardActivity.class)
                                .addFlags(
                                        Intent.FLAG_ACTIVITY_NO_ANIMATION
                                                | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));*/
                        handler.postDelayed(runnable, 10000);
                    }
                };

        handler.postDelayed(runnable, 15000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        handler.removeCallbacks(runnable);
        //        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        //        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }
}
