package fi.softala.tyokykypassi.adapters;

/**
 * Created by 1 on 13/10/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.models.Worksheet;
import fi.softala.tyokykypassi.models.WorksheetWaypoints;

public class KorttiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final List<WorksheetWaypoints> SubjectNames;
    final Worksheet mWorksheet;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private final KorttiAdapter.OnItemClickListener mListener;
    private final onRadioButtonCheckChange mChangeListener;
    private final KorttiAdapter.OnTextChangeListener mTextListener;
    private final KorttiAdapter.OnClickListener mClickListener;
    private final KorttiAdapter.OnTallennaListener mTallennaListener;

    public KorttiAdapter(Worksheet worksheet,
                         KorttiAdapter.OnItemClickListener listener,
                         KorttiAdapter.onRadioButtonCheckChange mlgListener,
                         KorttiAdapter.OnTextChangeListener textChangeListener,
                         KorttiAdapter.OnClickListener clickListener,
                         KorttiAdapter.OnTallennaListener tallennaListener) {
        this.mWorksheet = worksheet;
        this.SubjectNames = worksheet.getWorksheetWaypoints();
        this.mListener = listener;
        this.mChangeListener = mlgListener;
        this.mTextListener = textChangeListener;
        this.mClickListener = clickListener;
        this.mTallennaListener = tallennaListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_etappi, viewGroup, false);
            return new KorttiAdapter.ViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_lahetys, viewGroup, false);
            return new KorttiAdapter.FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof KorttiAdapter.FooterViewHolder) {
            KorttiAdapter.FooterViewHolder footerHolder = (KorttiAdapter.FooterViewHolder) holder;
            footerHolder.bind(mClickListener, mTallennaListener);
        } else if (holder instanceof KorttiAdapter.ViewHolder) {
            KorttiAdapter.ViewHolder viewHolder = (KorttiAdapter.ViewHolder) holder;
            WorksheetWaypoints waypoints = SubjectNames.get(position);
            viewHolder.bind(position, waypoints, mListener, mChangeListener, mTextListener, mWorksheet);
        }
    }


    @Override
    public int getItemCount() {
        return SubjectNames.size() + 1;
    }

    public interface OnItemClickListener {
        void onItemClick(WorksheetWaypoints points, Context context, ImageButton camera);
    }

    public interface onRadioButtonCheckChange {
        void onCheck(int waypointId, int radioID);
    }

    public interface OnTextChangeListener {
        void onTextChange(int waypointId, String text);
    }

    public interface OnClickListener {
        void onClick();
    }

    public interface OnTallennaListener {
        void onClick();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final EditText editText;
        final ImageButton camera;
        final Context context;
        final RadioGroup radioGroup;
        final RadioButton button1;
        final RadioButton button2;
        final RadioButton button3;
        final EditText selostus;

        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            editText = (EditText) view.findViewById(R.id.editTextEtappi);
            camera = (ImageButton) view.findViewById(R.id.kameraButton1);
            radioGroup = (RadioGroup) view.findViewById(R.id.etappi_radiogroup);
            button1 = (RadioButton) view.findViewById(R.id.etappi_radiobutton_green);
            button2 = (RadioButton) view.findViewById(R.id.etappi_radiobutton_yellow);
            button3 = (RadioButton) view.findViewById(R.id.etappi_radiobutton_red);
            selostus = (EditText) view.findViewById(R.id.selostusKentta1);


        }

        /*
            Bindaa tiettyihin nappuloihin tietyt tekstit, id:t ja kuuntelijat
            id:t ja tekstit löytyvät etapin tiedoista hakemalla
         */
        public void bind(int position, final WorksheetWaypoints waypoints,
                         final OnItemClickListener mListener,
                         final onRadioButtonCheckChange mChangeListener,
                         final OnTextChangeListener mTextListener,
                         final Worksheet worksheet) {
            final int id = waypoints.getWaypointID();

            final boolean cameraValinta = waypoints.getWaypointPhotoEnabled();
            if (!cameraValinta) {
                camera.setVisibility(View.GONE);
            }
            if (waypoints.getWanhaKuvaUrl() != null) {
                camera.setBackgroundResource(R.drawable.thumb_up);
                camera.setEnabled(false);
            }

            button1.setId(waypoints.getWaypointOptions().get(0).getOptionID());
            button2.setId(waypoints.getWaypointOptions().get(1).getOptionID());
            button3.setId(waypoints.getWaypointOptions().get(2).getOptionID());

            button1.setText(waypoints.getWaypointOptions().get(0).getOptionText());
            button2.setText(waypoints.getWaypointOptions().get(1).getOptionText());
            button3.setText(waypoints.getWaypointOptions().get(2).getOptionText());

            //Katso onko aurinko, kilpi tai sydän
            String radio1 = waypoints.getWaypointOptions().get(0).getOptionText();
            String radio2 = waypoints.getWaypointOptions().get(1).getOptionText();
            String radio3 = waypoints.getWaypointOptions().get(2).getOptionText();

            //Poista muotoilu radioButtonista

            Boolean neutraaliRadio;

            neutraaliRadio = isNeutraaliRadio(worksheet);

            if (!neutraaliRadio) {
                button1.setButtonDrawable(R.drawable.greenfaceselector);
                button2.setButtonDrawable(R.drawable.yellowfaceselector);
                button3.setButtonDrawable(R.drawable.redfaceselector);
            }

            final String tyhja = "";

            if (radio1.equalsIgnoreCase("aurinko")) {
                button1.setButtonDrawable(R.drawable.aurinkoselector);
                button1.setText(tyhja);
            }
            if (radio2.equalsIgnoreCase("kilpi")) {
                button2.setButtonDrawable(R.drawable.kilpiselector);
                button2.setText(tyhja);
            }
            if (radio3.equalsIgnoreCase("sydän")) {
                button3.setButtonDrawable(R.drawable.sydanselector);
                button3.setText(tyhja);
            }

            Boolean isPeukku = isPeukku(worksheet);

            if (isPeukku) {
                button1.setButtonDrawable(R.drawable.thumb_up_selector);
                button2.setButtonDrawable(R.drawable.hand_selector);
                button3.setButtonDrawable(R.drawable.thumb_down_selector);
            }


            if (waypoints.getWaypointOptions().get(0).getOptionID() == waypoints.getWanhaRadioButtonValinta()) {
                button1.setChecked(true);
            } else if (waypoints.getWaypointOptions().get(1).getOptionID() == waypoints.getWanhaRadioButtonValinta()) {
                button2.setChecked(true);
            } else if (waypoints.getWaypointOptions().get(2).getOptionID() == waypoints.getWanhaRadioButtonValinta()) {
                button3.setChecked(true);
            }

            editText.setText(waypoints.getWaypointTask());

            if (waypoints.getWanhaVastaus() != null) {
                selostus.setText(waypoints.getWanhaVastaus());
            }
            selostus.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mTextListener.onTextChange(id, selostus.getText().toString());
                    Log.v("Passi", "Text changed its= " + selostus.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == -1) {
                        Log.v("Passi", "Android bug");
                    } else {
                        Log.v("Passi", "Painettu! waypointid = " + id + " radiobutton id " + checkedId);
                        mChangeListener.onCheck(id, checkedId);
                    }
                }
            });

            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(waypoints, context, camera);
                }
            });
        }

        private boolean isPeukku(Worksheet worksheet) {

            final String peukku1 = "Taukoliikuntaa";
            final String peukku2 = "Liikutko sinä riittävästi?";
            final String peukku3 = "Oma tapa liikkua";
            final String peukku4 = "Syö hyvin";
            final String peukku5 = "Ryhmän rakentaminen";
            final String peukku6 = "Suomalaisuus";

            boolean valitaankoPeukku = false;
            String header = worksheet.getWorksheetHeader();

            if (header.equals(peukku1) ||
                    header.equals(peukku2) ||
                    header.equals(peukku3) ||
                    header.equals(peukku4) ||
                    header.equals(peukku5) ||
                    header.equals(peukku6)) {
                valitaankoPeukku = true;
            }

            return valitaankoPeukku;
        }

        private boolean isNeutraaliRadio(Worksheet worksheet) {

            final String neturaali1 = "Asikastilanteet";
            final String neturaali2 = "Ergonomia";
            final String neturaali3 = "Ohjeiden laatiminen";
            final String neturaali4 = "Työväline esittely";
            final String neturaali5 = "Juominen ja biletys";
            final String neturaali6 = "Juominen ja sosiaaliset suhteet";
            final String neturaali7 = "Arvot ja asenteet";
            final String neturaali8 = "Viihtyisä oppilaitos";
            final String neturaali9 = "Mihin käytät aikaasi: Ensimmäinen osa";
            final String neturaali10 = "Työhyvinvointia tukevat keinot";

            Boolean onkoNeutraali = false;
            String header = worksheet.getWorksheetHeader();
            if (header.equals(neturaali1) ||
                    header.equals(neturaali2) ||
                    header.equals(neturaali3) ||
                    header.equals(neturaali4) ||
                    header.equals(neturaali5) ||
                    header.equals(neturaali6) ||
                    header.equals(neturaali7) ||
                    header.equals(neturaali8) ||
                    header.equals(neturaali9) ||
                    header.equals(neturaali10)

                    ) {
                onkoNeutraali = true;
            }

            return onkoNeutraali;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position == SubjectNames.size();
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        final Button lahetaNappula;
        final Button tallennaNappula;

        public FooterViewHolder(View itemView) {
            super(itemView);
            lahetaNappula = (Button) itemView.findViewById(R.id.lahetaNappula);
            tallennaNappula = (Button) itemView.findViewById(R.id.tallennaNappula);
        }

        public void bind(final KorttiAdapter.OnClickListener mClickListener, final KorttiAdapter.OnTallennaListener mTallennaListener) {
            lahetaNappula.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onClick();
                }
            });
            tallennaNappula.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTallennaListener.onClick();
                }
            });
        }

    }


}

