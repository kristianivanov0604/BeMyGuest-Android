package com.be.my.guest.adapters;
import com.be.my.guest.R;
import com.be.my.guest.utility.BmgUtils;

import java.util.ArrayList;


import android.R.drawable;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter class for Events ListView
 * 
 */
public class TicketListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> items;
	private LayoutInflater inflater;
	
	public TicketListAdapter(Context context, ArrayList<String> items) {
		this.inflater = LayoutInflater.from(context);
		this.items = items;
		this.context = context;
	}

	@Override
	public int getCount() {
		return 3;
//		return items.size();
	}

	@Override
	public String getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.list_item_ticket, parent, false);
		}
		if (vi != null) {
			TextView tvIndex = (TextView)vi.findViewById(R.id.tv_ticket_id);
			TextView tvTitle = (TextView)vi.findViewById(R.id.tv_title);
			EditText etName = (EditText)vi.findViewById(R.id.et_name).findViewById(R.id.et_text);
			EditText etCPF = (EditText)vi.findViewById(R.id.et_rg_cpf).findViewById(R.id.et_text);
			
			tvIndex.setText(String.format("%d %s", position+1, context.getResources().getString(R.string.TICKET)));
			etName.setHint(R.string.Complete_Name);
			etName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
			etCPF.setHint(R.string.RG_CPF);
			etCPF.setInputType(InputType.TYPE_CLASS_NUMBER);

		}
		return vi;
	}
	
}
