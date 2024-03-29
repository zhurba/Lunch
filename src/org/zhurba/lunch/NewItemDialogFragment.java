package org.zhurba.lunch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class NewItemDialogFragment extends DialogFragment {
	
	public interface NewItemDialogListener {
        public void onDialogPositiveClick(String itemName);
    }
	
	NewItemDialogListener mListener;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NewItemDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NewItemDialogListener");
        }
    }
	
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        //Get the layout inflater
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_newitem, null);
        final EditText newItem = (EditText) view.findViewById(R.id.text_newitem);
        		
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder
        	.setView(view)
        	.setTitle(R.string.newitem_dlg_title)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int id) {
            		mListener.onDialogPositiveClick(newItem.getText().toString());
                }
            })
            .setNegativeButton(R.string.action_cancel, null);
        // Create the AlertDialog object and return it
        return builder.create();
	}


}
