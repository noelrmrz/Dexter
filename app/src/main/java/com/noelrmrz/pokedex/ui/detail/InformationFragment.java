package com.noelrmrz.pokedex.ui.detail;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;
import com.noelrmrz.pokedex.pojo.AbilityLink;
import com.noelrmrz.pokedex.pojo.EvolutionChain;
import com.noelrmrz.pokedex.pojo.EvolutionChainLink;
import com.noelrmrz.pokedex.pojo.FlavorTextEntry;
import com.noelrmrz.pokedex.pojo.Genera;
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.pojo.Type;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.utilities.GsonClient;
import com.noelrmrz.pokedex.utilities.HelperTools;
import com.noelrmrz.pokedex.utilities.PicassoClient;
import com.noelrmrz.pokedex.utilities.RetrofitClient;
import com.noelrmrz.pokedex.utilities.TypeEffectiveness;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.view.Gravity.CENTER;

public class InformationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    static Pokemon savedPokemon;
    private final String LANGUAGE = "EN";
    private final String VERSION_NAME = "omega-ruby";
    private TypeEffectiveness typeEffectiveness = TypeEffectiveness.getInstance();

    public InformationFragment() {
    }

    public static InformationFragment newInstance(String param1) {
        InformationFragment informationFragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        informationFragment.setArguments(args);
        return informationFragment;
    }

    /*
    Called when the fragment is being created or recreated
    Use onCreate for any standard setup that does not require the activity to be fully created
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            savedPokemon = GsonClient.getGsonClient().fromJson(mParam1, Pokemon.class);
        }

    }

    /*
    Called when the fragment should create its view object heirarchy
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    /*
    Triggered after onCreateView
    only called if the view from onCreateView is non-null
    View setup should occur here
    */
    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        TextView description = view.findViewById(R.id.tv_description);
        TextView weight = view.findViewById(R.id.tv_weight);
        TextView height = view.findViewById(R.id.tv_height);

        TextView primaryAbility = view.findViewById(R.id.tv_ability_primary_name);
        TextView secondaryAbility = view.findViewById(R.id.tv_ability_secondary_name);
        TextView hiddenAbility = view.findViewById(R.id.tv_ability_hidden_name);
        TextView primaryAbilityType = view.findViewById(R.id.tv_ability_primary_type);
        TextView secondaryAbilityType = view.findViewById(R.id.tv_ability_secondary_type);
        TextView hiddenAbilityType = view.findViewById(R.id.tv_ability_hidden_type);

        ImageView base = view.findViewById(R.id.iv_first_stage);
        ImageView intermediate = view.findViewById(R.id.iv_second_stage);
        ImageView final_evolution = view.findViewById(R.id.iv_third_stage);

        TextView xFour = (TextView) view.findViewById(R.id.tv_xFour);
        FlexboxLayout xFourFlexLayout = (FlexboxLayout) view.findViewById(R.id.xFour_flexLayout);
        LinearLayout xFourLinearLayout = view.findViewById(R.id.xFour_layout);

        TextView xTwo = (TextView) view.findViewById(R.id.tv_xTwo);
        FlexboxLayout xTwoFlexLayout = (FlexboxLayout) view.findViewById(R.id.xTwo_flexLayout);
        LinearLayout xTwoLinearLayout = view.findViewById(R.id.xTwo_layout);

        TextView xOne = (TextView) view.findViewById(R.id.tv_xOne);
        FlexboxLayout xOneFlexLayout = (FlexboxLayout) view.findViewById(R.id.xOne_flexLayout);
        LinearLayout xOneLinearLayout = view.findViewById(R.id.xOne_layout);

        TextView xHalf = (TextView) view.findViewById(R.id.tv_xHalf);
        FlexboxLayout xHalfFlexLayout = (FlexboxLayout) view.findViewById(R.id.xHalf_flexLayout);
        LinearLayout xHalfLinearLayout = view.findViewById(R.id.xHalf_layout);

        TextView xQuarter = (TextView) view.findViewById(R.id.tv_xQuarter);
        FlexboxLayout xQuarterFlexLayout = (FlexboxLayout) view.findViewById(R.id.xQuarter_flexLayout);
        LinearLayout xQuarterLinearLayout = view.findViewById(R.id.xQuarter_layout);

        TextView xZero = (TextView) view.findViewById(R.id.tv_xZero);
        FlexboxLayout xZeroFlexLayout = (FlexboxLayout) view.findViewById(R.id.xZero_flexLayout);
        LinearLayout xZeroLinearLayout = view.findViewById(R.id.xZero_layout);

        setTypeMultipliers(savedPokemon.getTypeList().length, xFour, xFourFlexLayout, xFourLinearLayout,
                xTwo, xTwoFlexLayout, xTwoLinearLayout,
                xOne, xOneFlexLayout, xOneLinearLayout,
                xHalf, xHalfFlexLayout, xHalfLinearLayout,
                xQuarter, xQuarterFlexLayout,xQuarterLinearLayout,
                xZero, xZeroFlexLayout, xZeroLinearLayout);

        for (AbilityLink ability: savedPokemon.getAbilityList()) {
            switch (ability.getSlot()) {
                case 1:
                    primaryAbility.setText(ability.getAbility().getName());
                    break;
                case 2:
                    secondaryAbility.setVisibility(View.VISIBLE);
                    secondaryAbilityType.setVisibility(View.VISIBLE);
                    secondaryAbility.setText(ability.getAbility().getName());
                    break;
                case 3:
                    hiddenAbility.setVisibility(View.VISIBLE);
                    hiddenAbilityType.setVisibility(View.VISIBLE);
                    hiddenAbility.setText(ability.getAbility().getName());
                    break;
            }
        }

        TextView species = view.findViewById(R.id.tv_species);

        species.setText(getLanguage(LANGUAGE, savedPokemon.getPokemonSpecies().getGenera()));
        description.setText(getSpeciesTextEntry(LANGUAGE,
                savedPokemon.getPokemonSpecies().getFlavorTextEntries()));
        weight.setText(String.valueOf(savedPokemon.getWeight()) + "lbs");
        height.setText((String.valueOf(savedPokemon.getHeight())));
        String evolutionLink = savedPokemon.getPokemonSpecies().getEvolutionChainLink().getUrl().substring(41);
        String evolutionChainId = evolutionLink.replace("/", "").trim();

        RetrofitClient.getPokemonEvolutionChain(new Callback<EvolutionChainLink>() {
            @Override
            public void onResponse(Call<EvolutionChainLink> call,
                                   Response<EvolutionChainLink> response) {
                // Set the image for the first evolution stage
                EvolutionChain firstStage = response.body().getChain();
                String idOne = firstStage.getSpecies().getUrl().substring(42).replace("/", "").trim();
                PicassoClient.downloadSpriteImage(idOne, base);

                // Check and set the second evolution stage
                if (firstStage.getNextEvolutions().length > 0) {
                    EvolutionChain secondStage = firstStage.getNextEvolutions()[0];
                    String idTwo = secondStage.getSpecies().getUrl().substring(42).replace("/", "").trim();
                    intermediate.setVisibility(View.VISIBLE);
                    PicassoClient.downloadSpriteImage(idTwo, intermediate);

                    // Check and set for the third evolution stage
                    if (secondStage.getNextEvolutions().length > 0) {
                        EvolutionChain thirdStage = secondStage.getNextEvolutions()[0];
                        String idThree = thirdStage.getSpecies().getUrl().substring(42).replace("/", "").trim();
                        final_evolution.setVisibility(View.VISIBLE);
                        PicassoClient.downloadSpriteImage(idThree, final_evolution);
                    }
                }
            }

            @Override
            public void onFailure(Call<EvolutionChainLink> call, Throwable t) {
                Timber.d(t);
            }
        }, evolutionChainId);
    }

    private String getLanguage(String language, Genera[] generaList) {
        for (Genera genera : generaList) {
            if (genera.getLanguage().getLanguage().equalsIgnoreCase(language))
                return genera.getGenus();
        }
        return language;
    }

    private String getSpeciesTextEntry(String language, FlavorTextEntry[] textEntries) {
        for (FlavorTextEntry entry : textEntries) {
            if (entry.getVersionGroup().getName().equalsIgnoreCase(VERSION_NAME)
                    && entry.getLanguage().getLanguage().equalsIgnoreCase(language)) {
                return entry.getFlavorText().replaceAll("(\n)", " ");
            }
        }
        return language;
    }

    private void addTypeEffectiveness(FlexboxLayout layout, List<String> list) {
        for (int y = 0; y < list.size(); y++) {
            // create a new textview
            TextView rowTextView = new TextView(getActivity());

            // set the background
            rowTextView.setBackgroundResource(R.drawable.rectangle);

            // set the background color
            GradientDrawable typeBackground = (GradientDrawable) rowTextView.getBackground();
            typeBackground.setColor(getContext().getResources().getColor(HelperTools
                    .getColor(getContext(), list.get(y))));

            // set the text
            rowTextView.setText(list.get(y));

            // set the text color based on luminance
            rowTextView.setTextColor(HelperTools
                    .getTextColor(HelperTools.getColor(getContext(), list.get(y))));

            // set the padding
            float paddingTopBottom = HelperTools.convertDpToPixel(8, getContext());
            float paddingLeftRight = HelperTools.convertDpToPixel(2, getContext());
            rowTextView.setPadding((int) paddingTopBottom,
                    (int) paddingLeftRight, (int) paddingTopBottom
                    , (int) paddingLeftRight);

            FlexboxLayout.LayoutParams params= new FlexboxLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            int margins = (int) HelperTools.convertDpToPixel(1, getContext());
            params.setMargins(margins, margins, margins, margins);

            rowTextView.setGravity(CENTER);

            // set the size
            rowTextView.setTextSize(12);

            // set our parameters to the TextView
            rowTextView.setLayoutParams(params);

            // add the TextView to the LinearLayout
            layout.addView(rowTextView);
        }
    }

    private void setTypeMultipliers(int typeLength, TextView xFour, FlexboxLayout xFourFlexLayout, LinearLayout xFourLinearLayout,
                                    TextView xTwo, FlexboxLayout xTwoFlexLayout, LinearLayout xTwoLinearLayout,
                                    TextView xOne, FlexboxLayout xOneFlexLayout, LinearLayout xOneLinearLayout,
                                    TextView xHalf, FlexboxLayout xHalfFlexLayout, LinearLayout xHalfLinearLayout,
                                    TextView xQuarter, FlexboxLayout xQuarterFlexLayout, LinearLayout xQuarterLinearLayout,
                                    TextView xZero, FlexboxLayout xZeroFlexLayout, LinearLayout xZeroLinearLayout) {

        RetrofitClient.getTypeInformation(new Callback<Type>() {
            @Override
            public void onResponse(Call<Type> call, Response<Type> response) {
                Type primary = response.body();

                if ( typeLength == 1) {
                    savedPokemon = typeEffectiveness.setTypeEffectiveness(primary, null, savedPokemon);
                    addTypeEffectiveness(xTwoFlexLayout, savedPokemon.getEffective());
                    addTypeEffectiveness(xHalfFlexLayout, savedPokemon.getNotEffective());
                    /*addTypeEffectiveness(xZeroFlexLayout, savedPokemon.getImmune());*/
                    addTypeEffectiveness(xOneFlexLayout, savedPokemon.getNormal());

                    xFour.setVisibility(View.GONE);
                    xFourFlexLayout.setVisibility(View.GONE);
                    xFourLinearLayout.setVisibility(View.GONE);

                    xQuarter.setVisibility(View.GONE);
                    xQuarterFlexLayout.setVisibility(View.GONE);
                    xQuarterLinearLayout.setVisibility(View.GONE);
                } else {
                    // Make a second call to get the information for the secondary type
                    RetrofitClient.getTypeInformation(new Callback<Type>() {
                        @Override
                        public void onResponse(Call<Type> call, Response<Type> response) {
                            Type secondary = response.body();
                            savedPokemon = typeEffectiveness.setTypeEffectiveness(primary, secondary, savedPokemon);
                            if (savedPokemon.getSuperEffective().size() == 0) {
                                xFour.setVisibility(View.GONE);
                                xFourFlexLayout.setVisibility(View.GONE);
                                xFourLinearLayout.setVisibility(View.GONE);
                            } else {
                                addTypeEffectiveness(xFourFlexLayout, savedPokemon.getSuperEffective());
                            }

                            if (savedPokemon.getEffective().size() == 0) {
                                xTwo.setVisibility(View.GONE);
                                xTwoFlexLayout.setVisibility(View.GONE);
                                xTwoLinearLayout.setVisibility(View.GONE);
                            } else {
                                addTypeEffectiveness(xTwoFlexLayout, savedPokemon.getEffective());
                            }

                            if (savedPokemon.getNormal().size() == 0) {
                                xOne.setVisibility(View.GONE);
                                xOneFlexLayout.setVisibility(View.GONE);
                                xOneLinearLayout.setVisibility(View.GONE);
                            } else {
                                addTypeEffectiveness(xOneFlexLayout, savedPokemon.getNormal());
                            }

                            if (savedPokemon.getNotEffective().size() == 0) {
                                xHalf.setVisibility(View.GONE);
                                xHalfFlexLayout.setVisibility(View.GONE);
                                xHalfLinearLayout.setVisibility(View.GONE);
                            } else {
                                addTypeEffectiveness(xHalfFlexLayout, savedPokemon.getNotEffective());
                            }

                            if (savedPokemon.getNotVeryEffective().size() == 0) {
                                xQuarter.setVisibility(View.GONE);
                                xQuarterFlexLayout.setVisibility(View.GONE);
                                xQuarterLinearLayout.setVisibility(View.GONE);
                            } else {
                                addTypeEffectiveness(xQuarterFlexLayout, savedPokemon.getNotVeryEffective());
                            }

                        }

                        @Override
                        public void onFailure(Call<Type> call, Throwable t) {
                            Timber.d(t);
                        }
                    }, savedPokemon.getTypeList()[1].getType().getName());
                }

                if (savedPokemon.getImmune().size() == 0) {
                    xZero.setVisibility(View.GONE);
                    xZeroFlexLayout.setVisibility(View.GONE);
                    xZeroLinearLayout.setVisibility(View.GONE);
                } else {
                    addTypeEffectiveness(xZeroFlexLayout, savedPokemon.getImmune());
                }
            }
            @Override
            public void onFailure(Call<Type> call, Throwable t) {
                Timber.d(t);
            }
        }, savedPokemon.getTypeList()[0].getType().getName());
    }
}
