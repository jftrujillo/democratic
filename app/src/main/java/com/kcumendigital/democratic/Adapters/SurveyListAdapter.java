package com.kcumendigital.democratic.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kcumendigital.democratic.Models.Survey;
import com.kcumendigital.democratic.R;
import com.kcumendigital.democratic.Util.ColletionsStatics;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by asus on 27/10/2015.
 */
public class SurveyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    static final int VIEW_SPAN = 0;
    static final int VIEW_PAGER = 3;

    boolean pagerEnabled;

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        if(pagerEnabled && ColletionsStatics.getHomeSurvey().size()>0)
            onItemClickListenerSurvey.onItemClick(position-1);
    }


    public interface OnItemClickListenerSurvey{
        void onItemClick (int position);
    }

    PagerAdpater pagerAdapter;
    Context context;
    OnItemClickListenerSurvey onItemClickListenerSurvey;
    FragmentManager fm;
    RecyclerView recyclerView;
    ArrayList<Survey> data;
    com.squareup.picasso.Transformation transformation;

    int sizeAvatar;

    public SurveyListAdapter(OnItemClickListenerSurvey onItemClickListenerSurvey, Context context, FragmentManager fm, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.onItemClickListenerSurvey = onItemClickListenerSurvey;
        this.context = context;
        this.fm = fm;
        this.data = ColletionsStatics.getDataSurvey();

        transformation = new RoundedTransformationBuilder()
                .oval(true)
                .build();

        sizeAvatar = context.getResources().getDimensionPixelSize(R.dimen.list_avatar);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_SPAN){
            View v = View.inflate(context,R.layout.survey_list_span_template, null);
            v.setOnClickListener(this);
            viewHolder = new SurveyListSpanViewHolder(v);
        }

        else if (viewType == VIEW_PAGER){
            View v = View.inflate(context,R.layout.template_pager_holder,null);
            viewHolder = new HomeListPagerHolder(v);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof SurveyListSpanViewHolder){
            SurveyListSpanViewHolder spanHolder = (SurveyListSpanViewHolder) holder;
            if(ColletionsStatics.getHomeSurvey().size()>0 && pagerEnabled)
                position = position - 1;
            spanHolder.title_survey_list.setText(data.get(position).getTitle());
            spanHolder.user_name.setText(data.get(position).getUser().getUserName());
            String BiggerOpcion = data.get(position).getOptions().get(0).getDescription();
            Long suma= Long.valueOf(0);
            Long BiggerOpcionNumber = Long.valueOf(0);
            for (int i = 0; i<data.get(position).getOptions().size(); i++){
                if (BiggerOpcionNumber < data.get(position).getOptions().get(i).getVotes()){
                    BiggerOpcionNumber = data.get(position).getOptions().get(i).getVotes();
                    BiggerOpcion = data.get(position).getOptions().get(i).getDescription();
                }
                suma = suma + data.get(position).getOptions().get(i).getVotes();
            }
            float porcentaje = 0;
            if (suma != 0) {

                porcentaje = BiggerOpcionNumber * 100 / suma;
            }
            else
            porcentaje = 0;
            spanHolder.percentage_opcion.setText(""+porcentaje);
            spanHolder.opcion_name.setText(BiggerOpcion);
            String categoria = data.get(position).getCategory();
            if (categoria.equals(context.getString(R.string.c_gobierno))) {
                Picasso.with(context).load(R.drawable.ic_account_balance_white_36dp).into(spanHolder.leftIcon);
                spanHolder.leftColor.setBackgroundResource(R.color.gobierno);

            }
            else if (categoria.equals(context.getString(R.string.c_salud))) {
                Picasso.with(context).load(R.drawable.ic_local_hospital_white_18dp).into(spanHolder.leftIcon);
                spanHolder.leftColor.setBackgroundResource(R.color.salud);

            }
            else if (categoria.equals(context.getString(R.string.c_educacion))) {
                Picasso.with(context).load(R.drawable.ic_school_white_18dp).into(spanHolder.leftIcon);
                spanHolder.leftColor.setBackgroundResource(R.color.educacion);

            }else if (categoria.equals(context.getString(R.string.c_ambiente))) {
                Picasso.with(context).load(R.drawable.ic_send_white_24dp).into(spanHolder.leftIcon);
                spanHolder.leftColor.setBackgroundResource(R.color.medio_ambiente);
            }

            Picasso.with(context).load(Uri.parse(data.get(position).getUser().getImg()))
                    .resize(sizeAvatar, sizeAvatar)
                    .centerCrop()
                    .transform(transformation).into(spanHolder.img);

        }

        else if (holder instanceof HomeListPagerHolder){
            HomeListPagerHolder pagerHolder = (HomeListPagerHolder) holder;
            pagerAdapter = new PagerAdpater(fm,pagerHolder.pager,pagerHolder.marksLayout,PagerAdpater.TYPE_SURVEY);
            pagerHolder.pager.setAdapter(pagerAdapter);
        }

    }

    @Override
    public int getItemCount() {

        if(pagerEnabled && ColletionsStatics.getHomeSurvey().size()>0)
            return data.size()+1;
        else
            return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && pagerEnabled && ColletionsStatics.getHomeSurvey().size()>0) {
            return  VIEW_PAGER;

        }

        else {
            return VIEW_SPAN;
        }
    }

    // region ViewHolders
    public  class SurveyListSpanViewHolder extends RecyclerView.ViewHolder{

        TextView title_survey_list,percentage_opcion,user_name,likes,dislikes,categoria,opcion_name;
        ImageView img,leftColor,leftIcon;


        public SurveyListSpanViewHolder(View itemView) {
            super(itemView);
            title_survey_list = (TextView) itemView.findViewById(R.id.title_forum_list);
            percentage_opcion = (TextView) itemView.findViewById(R.id.count_coments_forums);
            user_name = (TextView) itemView.findViewById(R.id.user_name_forums);
            likes = (TextView) itemView.findViewById(R.id.count_likes);
            dislikes = (TextView) itemView.findViewById(R.id.count_dislikes);
            categoria = (TextView) itemView.findViewById(R.id.categorias_list);
            img =   (ImageView) itemView.findViewById(R.id.user_img);
            opcion_name = (TextView) itemView.findViewById(R.id.text_opcion_survey);
            leftColor = (ImageView) itemView.findViewById(R.id.left_color_category);
            leftIcon = (ImageView) itemView.findViewById(R.id.left_image_categori);

        }
    }
    public class  HomeListPagerHolder extends RecyclerView.ViewHolder {

        ViewPager pager;
        LinearLayout marksLayout;
        public HomeListPagerHolder(View itemView) {
            super(itemView);
            pager = (ViewPager) itemView.findViewById(R.id.pager);
            marksLayout = (LinearLayout) itemView.findViewById(R.id.marks);
        }
    }



    //endregion

    public void setPagerEnabled(boolean pagerEnabled) {
        this.pagerEnabled = pagerEnabled;
    }
}
