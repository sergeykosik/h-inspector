package com.havrl.hinspector.activities;

import java.net.MalformedURLException;

import android.os.Bundle;

import com.havrl.hinspector.R;
import com.havrl.hinspector.models.Inspection;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

public class InspectionsActivity extends BaseActivity {
	
	/**
	 * Mobile Service Client reference
	 */
	private MobileServiceClient mClient;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspections);

		try {
			// Create the Mobile Service Client instance, using the provided
			// Mobile Service URL and key
			mClient = new MobileServiceClient(
					"https://h-inspector.azure-mobile.net/",
					"sxNQYmQxlsNdimZacNQtgxXOcxYorY19", this);

			Inspection inspection = new Inspection();

			mClient.getTable(Inspection.class).insert(inspection,
					new TableOperationCallback<Inspection>() {
						@Override
						public void onCompleted(Inspection entity,
								Exception exception,
								ServiceFilterResponse response) {
							if (exception == null) {
								// Insert succeeded
							} else {
								// Insert failed
							}

						}
					});

		} catch (MalformedURLException e) {
			createAndShowDialog(
					new Exception(
							"There was an error creating the Mobile Service. Verify the URL"),
					"Error");
		}

	}
}
