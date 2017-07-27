package th.co.thiensurat.tsr_history.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import th.co.thiensurat.tsr_history.full_authen.FullAuthenActivity;
import th.co.thiensurat.tsr_history.search.SearchActivity;


public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		try {
			String status = NetworkUtil.getConnectivityStatusString(context);
			if (status.equals("Wifi enabled") || status.equals("Mobile data enabled")) {
				FullAuthenActivity.getInstance().detectWifiConnected("connect");
			} else {
				FullAuthenActivity.getInstance().detectWifiConnected("not connect");
			}
		} catch (Exception e) {

		}
	}
}
