package urbanhack.reportapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.alexrs.wavedrawable.WaveDrawable;
import urbanhack.reportapp.HomeActivity;
import urbanhack.reportapp.SharedPreferenceUtil;

/**
 * Created by rohan on 3/10/15.
 */
public class SearchFragment extends Fragment implements View.OnClickListener, SwipeFlingAdapterView.onFlingListener, HomeActivity.SearchListener {

    private static SearchFragment instance;
    private View view;
    private SharedPreferenceUtil preferenceUtil;
    private HomeActivity activity;
    private RelativeLayout container;

    public static Fragment getInstance() {
        if (instance == null)
            instance = new SearchFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, null);
        TypefaceHelper.typeface(view);
        preferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        initView(view);
        return view;
    }

    private SwipeFlingAdapterView flingContainer;
    private TextView noCardsTv;
    private ImageView searchIV;
    private RelativeLayout searchView;
    private WaveDrawable waveDrawable;
    private int currentPage, totalPages;

    private CardAdapter adapter;
    private List<Card> cards;
    private ImageView like;
    private ImageView dislike;
    private List<Report> listOfReports = Collections.EMPTY_LIST;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView(View view) {

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_add_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ReportActivity.class));
            }
        });
        flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.cards_reports);
        cards = new ArrayList<>();
        adapter = new CardAdapter(getActivity(), cards);
        flingContainer.setAdapter(adapter);
        flingContainer.setFlingListener(this);
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int i, Object o) {
                Card card = (Card) flingContainer.getItemAtPosition(i);
                int pos = 99;
                for (int j = 0; j < listOfReports.size(); j++) {
                    String reportIdOfStoredList = listOfReports.get(j).getId();
                    if (reportIdOfStoredList.equalsIgnoreCase(card.getId())) {
                        pos = j;
                    }
                }
                if (pos != 99) {
                    Intent intent = new Intent(getActivity(), ReportSummaryActivity.class);
                    intent.putExtra("report", listOfReports.get(pos));
                    startActivityForResult(intent, AppConstants.ACITIVITY_REQUEST_CODES.PREVIEW_REPORT);
                }
            }
        });

        like = (ImageView) view.findViewById(R.id.iv_home_interested);
        dislike = (ImageView) view.findViewById(R.id.iv_home_not_interested);
        noCardsTv = (TextView) view.findViewById(R.id.tv_card_no_cards);

        searchIV = (ImageView) view.findViewById(R.id.iv_card_search_profile);
        searchView = (RelativeLayout) view.findViewById(R.id.rl_card_search_view);
        container = (RelativeLayout) view.findViewById(R.id.container_searchFragment);


        waveDrawable = new WaveDrawable(Color.parseColor("#121212"), 500);
        container.setBackground(waveDrawable);
        waveDrawable.setWaveInterpolator(new LinearInterpolator());

        startSearching();
//        stopSearchAfterDelay();

        dislike.setOnClickListener(this);
        like.setOnClickListener(this);


        Logger.logInfo("about to call get reports");
        ((HomeActivity) getActivity()).getReports(0);
    }


    private void hideSearching() {
        waveDrawable.stopAnimation();
        searchView.setVisibility(View.INVISIBLE);
        noCardsTv.setVisibility(View.INVISIBLE);
        flingContainer.setVisibility(View.VISIBLE);
        dislike.setEnabled(true);
        like.setEnabled(true);
        adapter.notifyDataSetChanged();
    }

    private void startSearching() {
        searchView.setVisibility(View.VISIBLE);
        flingContainer.setVisibility(View.INVISIBLE);
        noCardsTv.setVisibility(View.VISIBLE);
        waveDrawable.startAnimation();
        dislike.setEnabled(false);
        like.setEnabled(false);
        noCardsTv.setText("Searching reports around you.");
    }

    @Override
    public void onSearchCompleted(final List<Report> reports) {
        Logger.logInfo("Fetched these now rendering.."+reports);
        listOfReports = reports;
        if (listOfReports.size() != 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addCardsFromBackend(reports);
                }
            }, 2000);
        } else {
            noCardsTv.setText("No reports found..");
        }
    }


    @Override
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }


    private void addCardsFromBackend(List<Report> reports) {
        for (Report report : reports) {
            try {
                cards.add(new Card(report.getImgUrls().get(0), report.getAuthorImgUrl(),report.getTitle(),report.getCategory(), DateUtils.getFormatedDateString(report.getDateOfReport()),report.getAuthor(),report.getId()));
            } catch (Exception e) {
                Logger.logError("Exception oocurred while adding card "+e.getMessage());
            }
        }
        hideSearching();
    }


    @Override
    public void removeFirstObjectInAdapter() {
        Logger.logInfo("remove first");
        cards.remove(0);
        adapter.notifyDataSetChanged();
        if (cards.size() == 0)
            onCardsEmpty();
    }

    private void onCardsEmpty() {
        startSearching();
        currentPage = currentPage + 1;
        if (currentPage < totalPages) {
            //activity.getJobsPaginated(currentPage);
        } else {
            noCardsTv.setText("No reports found");
        }
    }

    @Override
    public void onLeftCardExit(Object o) {
        Logger.logInfo("onLeft CardExit");
    }

    @Override
    public void onRightCardExit(Object o) {
        Card card = (Card) o;
    }


    @Override
    public void onAdapterAboutToEmpty(int i) {
        Logger.logInfo("onAdapter AboutToEmpty");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onScroll(float v) {
        try {
            if (v == 1.0) {
                ImageView topCard = (ImageView) flingContainer.getSelectedView().findViewById(R.id.iv_card_interested_stamp);
                topCard.setVisibility(View.VISIBLE);
            } else if (v == -1.0) {
                ImageView topCard = (ImageView) flingContainer.getSelectedView().findViewById(R.id.iv_card_not_interested_stamp);
                topCard.setVisibility(View.VISIBLE);
            } else {
                (flingContainer.getSelectedView().findViewById(R.id.iv_card_interested_stamp)).setVisibility(View.INVISIBLE);
                (flingContainer.getSelectedView().findViewById(R.id.iv_card_not_interested_stamp)).setVisibility(View.INVISIBLE);
            }
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void dislike() {
        flingContainer.getSelectedView().findViewById(R.id.iv_card_not_interested_stamp).setVisibility(View.VISIBLE);
        flingContainer.getTopCardListener().selectLeft();
    }

    private void like() {
        flingContainer.getSelectedView().findViewById(R.id.iv_card_interested_stamp).setVisibility(View.VISIBLE);
        flingContainer.getTopCardListener().selectRight();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_home_interested:
                like();
                break;
            case R.id.iv_home_not_interested:
                dislike();
                break;
        }
    }


    private class CardAdapter extends BaseAdapter {
        private Activity activity;
        private List<Card> cardList;
        private ImageView interestedStampIv;
        private ImageView notInterestedStampIv;

        public CardAdapter(Activity activity, List<Card> cardList) {
            this.activity = activity;
            this.cardList = cardList;
        }

        @Override
        public int getCount() {
            return cardList.size();
        }

        @Override
        public Object getItem(int position) {
            return cardList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Card card = cardList.get(position);
            convertView = activity.getLayoutInflater().inflate(R.layout.card_report, parent, false);
            TypefaceHelper.typeface(convertView);
            CardView cardView = (CardView) convertView.findViewById(R.id.card_view);
            final ImageView mainIv = (ImageView) convertView.findViewById(R.id.iv_card_main_image);
            ImageView profilePicIV = (ImageView) convertView.findViewById(R.id.iv_profile_pic);
            TextView titleTV = (TextView) convertView.findViewById(R.id.tv_card_title);
            TextView categoryTV = (TextView) convertView.findViewById(R.id.tv_card_category);
            TextView postedOnTV = (TextView) convertView.findViewById(R.id.tv_card_postedon);
            TextView authorNameTV = (TextView) convertView.findViewById(R.id.tv_card_authorname);

            titleTV.setText(card.getTitle());
            categoryTV.setText(card.getCategory());
            postedOnTV.setText(card.getPostedOn());
            authorNameTV.setText(card.getPosterName());

            mainIv.setTag(mainIv.getId(), card.imageUrl);

            interestedStampIv = (ImageView) convertView.findViewById(R.id.iv_card_interested_stamp);
            notInterestedStampIv = (ImageView) convertView.findViewById(R.id.iv_card_not_interested_stamp);
            interestedStampIv.setVisibility(View.INVISIBLE);
            notInterestedStampIv.setVisibility(View.INVISIBLE);



            if (card.getImageUrl() != null && !card.getImageUrl().isEmpty())
                Picasso.with(getActivity()).load(card.getImageUrl()).placeholder(R.drawable.logo_bangalore_police).into(mainIv);
            if (card.getAuthorImageUrl() != null && !card.getAuthorImageUrl().isEmpty())
                Picasso.with(getActivity()).load(card.getAuthorImageUrl()).placeholder(R.drawable.icon_profile_placeholder).into(profilePicIV);


            makeStackOfCards(position, convertView, cardView);
            return convertView;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private void makeStackOfCards(int position, View convertView, CardView cardView) {
            switch (position) {
                case 0:
                    //convertView.setTranslationY(-15);
                    cardView.setCardElevation(4.0f);
                    break;
                case 1:
                    //convertView.setTranslationY(-5);
                    cardView.setCardElevation(2.0f);
                    break;
                default:
                    //convertView.setTranslationY(1);
                    cardView.setCardElevation(1.0f);
            }
        }
    }

    private class Card {
        String imageUrl, authorImageUrl,title,category,postedOn,posterName,id;

        public Card(String imageUrl, String authorImageUrl, String title, String category, String postedOn, String posterName, String id) {
            this.id = id;
            this.imageUrl = imageUrl;
            this.authorImageUrl = authorImageUrl;
            this.title = title;
            this.category = category;
            this.postedOn = postedOn;
            this.posterName = posterName;
        }



        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getAuthorImageUrl() {
            return authorImageUrl;
        }

        public void setAuthorImageUrl(String authorImageUrl) {
            this.authorImageUrl = authorImageUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getPostedOn() {
            return postedOn;
        }

        public void setPostedOn(String postedOn) {
            this.postedOn = postedOn;
        }

        public String getPosterName() {
            return posterName;
        }

        public void setPosterName(String posterName) {
            this.posterName = posterName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
