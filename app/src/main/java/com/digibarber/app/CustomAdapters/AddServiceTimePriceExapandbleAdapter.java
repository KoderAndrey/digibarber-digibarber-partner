package com.digibarber.app.CustomAdapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digibarber.app.Beans.ServicePriceTime;
import com.digibarber.app.Interfaces.SelectServiceExapndblePosition;
import com.digibarber.app.R;
import com.digibarber.app.activities.AddServicePriceTimeActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DIGIBARBER LTD on 23/9/17.
 */

public class AddServiceTimePriceExapandbleAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<ServicePriceTime>> expandableListDetail;
    ArrayList<String> galleryItems = new ArrayList<>(Arrays.asList("30", "45", "60", "90"));
    int selectdPosition;
    private int lastFocussedPosition = -1;
    private Handler handler = new Handler();
    LayoutInflater layoutInflater;
    private String priceText;
    SelectServiceExapndblePosition objExapndblePosition;
    public String ClickedNext = "no";
    HashMap<String, List<Boolean>> expandableListDetailState;
    LayoutInflater layoutInflaterGroup;


    String From = "addServiceAdapter";
    String mobile ;

    public AddServiceTimePriceExapandbleAdapter(AddServicePriceTimeActivity addServicePriceTimeActivity, List<String> expandableListTitle, HashMap<String, List<ServicePriceTime>> expandableListDetail, SelectServiceExapndblePosition objExapndblePosition, HashMap<String, List<Boolean>> expandableListDetailState, String From, String mobile) {

        this.context = addServicePriceTimeActivity;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.objExapndblePosition = objExapndblePosition;
        this.expandableListDetailState = expandableListDetailState;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflaterGroup = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.From = From;
        this.mobile = mobile;

    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.expandableListDetail.get(this.expandableListTitle.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.expandableListTitle.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.expandableListDetail.get(this.expandableListTitle.get(i)).get(i1);
    }

    @Override
    public int getGroupTypeCount() {
        return super.getGroupTypeCount();
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return super.getChildType(groupPosition, childPosition);
    }

    @Override
    public int getGroupType(int groupPosition) {
        return super.getGroupType(groupPosition);
    }

    @Override
    public int getChildTypeCount() {
        return super.getChildTypeCount();
    }


    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    public void notfiyAdapter() {
        notifyDataSetChanged();
    }

    public void clikedNext() {
        ClickedNext = "yes";
    }

    public boolean isEmpty() {
        notfiyAdapter();
        boolean isAnyEmpty = true;
        for (int i = 0; i < expandableListTitle.size(); i++) {

            for (int j = 0; j < expandableListDetailState.get(expandableListTitle.get(i)).size(); j++) {

                if (expandableListDetailState.get(expandableListTitle.get(i)).get(j).equals(false)) {
                    isAnyEmpty = false;
                }

            }

        }
        return isAnyEmpty;
    }

    @Override
    public View getGroupView(int i, boolean state, View view, ViewGroup viewGroup) {
        final GroupHolder holder;
        if (view == null) {
            holder = new GroupHolder();
            view = layoutInflaterGroup.inflate(R.layout.adapter_expandble_header, null);
            holder.listTitleTextView = (TextView) view.findViewById(R.id.tv_header);
            holder.grupIndictr = (ImageView) view.findViewById(R.id.iv_expand);
            holder.iv_line = (ImageView) view.findViewById(R.id.iv_line);
            view.setTag(holder);
        } else {
            holder = (GroupHolder) view.getTag();
        }
        String listTitle = (String) getGroup(i);
        holder.listTitleTextView.setText(listTitle);
        if (state) {
            holder.grupIndictr.setImageResource(R.mipmap.expand_minus);
            holder.iv_line.setVisibility(View.GONE);
        } else {
            holder.grupIndictr.setImageResource(R.mipmap.expand_plus);
            holder.iv_line.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public View getChildView(final int groupPos, final int childPos, boolean bol, View view, ViewGroup viewGroup) {
        ServicePriceTime expandedListText = (ServicePriceTime) getChild(groupPos, childPos);
        final ChildHolder holder;
        if (view == null) {
            holder = new ChildHolder();
            view = layoutInflater.inflate(R.layout.adapter_child_expanble, null);
            holder.expandedListTextView = (TextView) view.findViewById(R.id.expndable_item);
            holder.etPrice = (EditText) view.findViewById(R.id.etPrice);
            holder.simpleGallery = (Gallery) view.findViewById(R.id.galleryView);
            holder.child_list_cross = (ImageView) view.findViewById(R.id.child_list_cross);
            holder.ll_linear_edit_des = (LinearLayout) view.findViewById(R.id.ll_linear_edit_des);
            holder.tv_edit_description = (TextView) view.findViewById(R.id.tv_edit_description);
            holder.rl_price = (LinearLayout) view.findViewById(R.id.rl_price);
            holder.iv_green_tick = (ImageView) view.findViewById(R.id.iv_green_tick);
            holder.tv_enter_error_price = (TextView) view.findViewById(R.id.tv_enter_error_price);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }


        if (From != null && From.equalsIgnoreCase("Signup")) {
            holder.tv_edit_description.setText("Add description");
        } else {
            holder.tv_edit_description.setText("Add description");
        }

        holder.childPosition = childPos;
        holder.groupPosition = groupPos;

        holder.expandedListTextView.setText(expandedListText.subName);
        priceText = holder.etPrice.getText().toString();

       /* if (holder.etPrice.getText().toString().equalsIgnoreCase("")) {
            holder.etPrice.setText("");
        }*/


        holder.etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // getPriceValue(reset_icon, child, holder.etPrice, holder.rl_price);

                String price = holder.etPrice.getText().toString();

                if (price.replace(" ", "").length() > 0) {
                    holder.rl_price.setBackgroundResource(R.mipmap.edit_price_back_icon);
                    holder.tv_enter_error_price.setVisibility(View.GONE);

                } else {

                    holder.rl_price.setBackgroundResource(R.mipmap.group_3copy6);
                    holder.tv_enter_error_price.setVisibility(View.VISIBLE);
                    holder.rl_price.requestFocus();
                }
                if (price != null && !price.equalsIgnoreCase("") && !price.equalsIgnoreCase("null")) {
                    int pric = Integer.parseInt(price);
                    if (pric > 150) {
                        showPopupMaxPrice();
                        holder.etPrice.setText("");
                        String catName = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).catName;
                        String subName = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).subName;
                        String sub_category_id = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).sub_category_id;
                        String catDes = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).subDes;
                        String catTime = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).subTime;

                        expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).set(holder.childPosition, new ServicePriceTime(catName, sub_category_id, subName, holder.etPrice.getText().toString(), catTime, catDes,mobile));

                        if (holder.etPrice.getText().toString().length() > 0) {
                            expandableListDetailState.get(expandableListTitle.get(holder.groupPosition)).set(holder.childPosition, true);
                        } else {
                            expandableListDetailState.get(expandableListTitle.get(holder.groupPosition)).set(holder.childPosition, false);
                        }
                    } else {
                        String catName = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).catName;
                        String subName = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).subName;
                        String sub_category_id = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).sub_category_id;
                        String catDes = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).subDes;
                        String catTime = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).subTime;

                        expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).set(holder.childPosition, new ServicePriceTime(catName, sub_category_id, subName, holder.etPrice.getText().toString(), catTime, catDes,mobile));


                        if (holder.etPrice.getText().toString().length() > 0) {
                            expandableListDetailState.get(expandableListTitle.get(holder.groupPosition)).set(holder.childPosition, true);
                        } else {
                            expandableListDetailState.get(expandableListTitle.get(holder.groupPosition)).set(holder.childPosition, false);
                        }
                    }
                } else {
                    String catName = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).catName;
                    String subName = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).subName;
                    String sub_category_id = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).sub_category_id;
                    String catDes = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).subDes;
                    String catTime = expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).subTime;

                    expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).set(holder.childPosition, new ServicePriceTime(catName, sub_category_id, subName, holder.etPrice.getText().toString(), catTime, catDes, mobile));

                    if (holder.etPrice.getText().toString().length() > 0) {
                        expandableListDetailState.get(expandableListTitle.get(holder.groupPosition)).set(holder.childPosition, true);
                    } else {
                        expandableListDetailState.get(expandableListTitle.get(holder.groupPosition)).set(holder.childPosition, false);
                    }
                }
            }
        });


        //    holder.etPrice.addTextChangedListener(new CustomTextWatcher(holder.etPrice, groupPos, childPos, holder.rl_price));
        final ServiceTimeGalleryAdapter ga = new ServiceTimeGalleryAdapter(context, galleryItems);
        holder.simpleGallery.setAdapter(ga);
        holder.simpleGallery.setSpacing(10);
        if (expandedListText.subTime != null && !expandedListText.subTime.equalsIgnoreCase("")) {
            if (expandedListText.subTime.equalsIgnoreCase("30")) {
                holder.simpleGallery.setSelection(0);
                //ga.selectedPosition(0);
                //ga.notifyDataSetChanged();
            } else if (expandedListText.subTime.equalsIgnoreCase("45")) {
                holder.simpleGallery.setSelection(1);
                //ga.selectedPosition(1);
                //ga.notifyDataSetChanged();
            } else if (expandedListText.subTime.equalsIgnoreCase("60")) {
                holder.simpleGallery.setSelection(2);
                //ga.selectedPosition(2);
                //ga.notifyDataSetChanged();

            } else if (expandedListText.subTime.equalsIgnoreCase("90")) {
                holder.simpleGallery.setSelection(3);
                //ga.selectedPosition(3);
                // ga.notifyDataSetChanged();
            }

        }


        holder.simpleGallery.setOnItemSelectedListener(new customOnGalleryItemSelcted(ga, groupPos, childPos));
        holder.child_list_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).remove(holder.childPosition);
                expandableListDetailState.get(expandableListTitle.get(holder.groupPosition)).remove(holder.childPosition);

                if (expandableListDetail.get(expandableListTitle.get(holder.groupPosition)).size() <= 0) {
                    expandableListTitle.remove(holder.groupPosition);
                }
                notfiyAdapter();
            }
        });

        holder.ll_linear_edit_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                objExapndblePosition.onDesSelcted(holder.groupPosition, holder.childPosition);

            }
        });


        String price = expandableListDetail.get(expandableListTitle.get(groupPos)).get(childPos).subPrice;
        holder.etPrice.setText(price.replace("£", ""));

        String des = expandableListDetail.get(expandableListTitle.get(groupPos)).get(childPos).subDes;

        if (des != null && des.length() > 0) {
            holder.iv_green_tick.setVisibility(View.VISIBLE);
        } else {
            holder.iv_green_tick.setVisibility(View.GONE);
        }

        if (ClickedNext.equalsIgnoreCase("yes")) {
            if (expandableListDetailState.get(expandableListTitle.get(holder.groupPosition)).get(holder.childPosition).equals(false)) {
                holder.rl_price.setBackgroundResource(R.mipmap.group_3copy6);
                holder.tv_enter_error_price.setVisibility(View.VISIBLE);
                holder.rl_price.requestFocus();
            } else {
                holder.rl_price.setBackgroundResource(R.mipmap.edit_price_back_icon);
                holder.tv_enter_error_price.setVisibility(View.GONE);
            }
        } else {
            holder.rl_price.setBackgroundResource(R.mipmap.edit_price_back_icon);
            holder.tv_enter_error_price.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    public class ChildHolder {
        TextView expandedListTextView;
        EditText etPrice;
        Gallery simpleGallery;
        ImageView child_list_cross;
        LinearLayout ll_linear_edit_des;
        LinearLayout rl_price;
        int groupPosition;
        int childPosition;
        TextView tv_edit_description;
        ImageView iv_green_tick;
        TextView tv_enter_error_price;
    }


    public class GroupHolder {
        TextView listTitleTextView;
        ImageView grupIndictr;
        ImageView iv_line;
    }


    private class customOnGalleryItemSelcted implements AdapterView.OnItemSelectedListener {
        int groupPos;
        int chilPos;
        ServiceTimeGalleryAdapter mGallery;


        public customOnGalleryItemSelcted(ServiceTimeGalleryAdapter mGallery, int groupPos, int chilPos) {
            this.groupPos = groupPos;
            this.chilPos = chilPos;
            this.mGallery = mGallery;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


            String catName = expandableListDetail.get(expandableListTitle.get(groupPos)).get(chilPos).catName;
            String subName = expandableListDetail.get(expandableListTitle.get(groupPos)).get(chilPos).subName;
            String sub_category_id = expandableListDetail.get(expandableListTitle.get(groupPos)).get(chilPos).sub_category_id;
            String catDes = expandableListDetail.get(expandableListTitle.get(groupPos)).get(chilPos).subDes;
            String catPrice = expandableListDetail.get(expandableListTitle.get(groupPos)).get(chilPos).subPrice;
            expandableListDetail.get(expandableListTitle.get(groupPos)).set(chilPos, new ServicePriceTime(catName, sub_category_id, subName, catPrice, galleryItems.get(position), catDes, mobile));
            mGallery.selectedPosition(position);
            mGallery.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }


    private class CustomTextWatcher implements TextWatcher {
        int groupPos;
        int chilPos;
        EditText mEditext;
        LinearLayout rl_price;

        public CustomTextWatcher(EditText mEditext, int groupPos, int chilPos, LinearLayout rl_price) {
            this.groupPos = groupPos;
            this.chilPos = chilPos;
            this.mEditext = mEditext;
            this.rl_price = rl_price;

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {

            if (mEditext.getText().length() > 0) {
                expandableListDetailState.get(expandableListTitle.get(groupPos)).set(chilPos, true);
            } else {
                expandableListDetailState.get(expandableListTitle.get(groupPos)).set(chilPos, false);
            }
            //updatePriceValue("£" + mEditext.getText().toString(), groupPos, chilPos);
        }
    }

    public void showPopupMaxPrice() {
        final Dialog dialog_first = new Dialog(context);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_max_price);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();
            }
        });


        dialog_first.show();
    }


}