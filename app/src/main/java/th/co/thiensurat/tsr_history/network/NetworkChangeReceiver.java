package th.co.thiensurat.tsr_history.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import th.co.thiensurat.tsr_history.search.SearchActivity;


public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		try {
			String status = NetworkUtil.getConnectivityStatusString(context);
			if (status.equals("Wifi enabled") || status.equals("Mobile data enabled")) {
				SearchActivity.getInstance().detectWifiConnected("connect");
			} else {
				SearchActivity.getInstance().detectWifiConnected("not connect");
			}
		} catch (Exception e) {

		}
	}
}
