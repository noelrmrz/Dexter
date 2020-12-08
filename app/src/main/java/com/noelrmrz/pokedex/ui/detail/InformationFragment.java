package com.noelrmrz.pokedex.ui.detail;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.databinding.FragmentInformationBinding;
import com.noelrmrz.pokedex.pojo.EvolutionChain;
import com.noelrmrz.pokedex.pojo.EvolutionChainLink;
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.pojo.Type;
import com.noelrmrz.pokedex.utilities.BindingAdapters;
import com.noelrmrz.pokedex.utilities.GlideClient;
import com.noelrmrz.pokedex.utilities.GsonClient;
import com.noelrmrz.pokedex.utilities.HelperTools;
import com.noelrmrz.pokedex.utilities.RetrofitClient;
import com.noelrmrz.pokedex.utilities.TypeEffectiveness;
import com.noelrmrz.pokedex.viewmodel.MainViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.view.Gravity.CENTER;

public class InformationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    static Pokemon savedPokemon;
    private TypeEffectiveness typeEffectiveness = TypeEffectiveness.getInstance();
    private FragmentInformationBinding bind;
    private MainViewModel mainViewModel;

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
            String mParam1 = getArguments().getString(ARG_PARAM1);
            savedPokemon = GsonClient.getGsonClient().fromJson(mParam1, Pokemon.class);
        }

    }

    /*
    Called when the fragment should create its view object heirarchy
    */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind = FragmentInformationBinding.inflate(inflater, container, false);

        bind.setPokemon(savedPokemon);
        bind.executePendingBindings();
        return bind.getRoot();
    }

    /*
    Triggered after onCreateView
    only called if the view from onCreateView is non-null
    View setup should occur here
    */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstance) {

        // data binding displays wrong data when using with view pager
        BindingAdapters.setPokemonDescription(bind.tvDescription, savedPokemon.getPokemonSpecies().getFlavorTextEntries());
        bind.tvWeight.setText(String.valueOf(savedPokemon.getWeight()));
        bind.tvHeight.setText(String.valueOf(savedPokemon.getHeight()));
        BindingAdapters.setPokemonGenus(bind.tvSpecies, savedPokemon.getPokemonSpecies().getGenera());

        BindingAdapters.setAbilities(bind.tvAbilityPrimaryName, savedPokemon.getAbilityList()[0]);
        BindingAdapters.setAbilities(bind.tvAbilitySecondaryName,
                savedPokemon.getAbilityList().length > 1 ? savedPokemon.getAbilityList()[1] : null);
        BindingAdapters.setAbilities(bind.tvAbilityHiddenName,
                savedPokemon.getAbilityList().length > 2 ? savedPokemon.getAbilityList()[2] : null);

        setTypeMultipliers(savedPokemon.getTypeList().length, bind.tvXFour, bind.xFourFlexLayout, bind.xFourLayout,
                bind.tvXTwo, bind.xTwoFlexLayout, bind.xTwoLayout,
                bind.tvXOne, bind.xOneFlexLayout, bind.xOneLayout,
                bind.tvXHalf, bind.xHalfFlexLayout, bind.xHalfLayout,
                bind.tvXQuarter, bind.xQuarterFlexLayout,bind.xQuarterLayout,
                bind.tvXZero, bind.xZeroFlexLayout, bind.xZeroLayout);

        String evolutionLink = savedPokemon.getPokemonSpecies().getEvolutionChainLink().getUrl().substring(41);
        String evolutionChainId = evolutionLink.replace("/", "").trim();

        RetrofitClient.getPokemonEvolutionChain(new Callback<EvolutionChainLink>() {
            @Override
            public void onResponse(Call<EvolutionChainLink> call,
                                   Response<EvolutionChainLink> response) {
                // Set the image for the first evolution stage
                EvolutionChain firstStage = response.body().getChain();
                String idOne = firstStage.getSpecies().getUrl().substring(42).replace("/", "").trim();
                GlideClient.downloadSpriteImage(idOne, bind.ivFirstStage);

                // Check and set the second evolution stage
                if (firstStage.getNextEvolutions().length > 0) {
                    EvolutionChain secondStage = firstStage.getNextEvolutions()[0];
                    String idTwo = secondStage.getSpecies().getUrl().substring(42).replace("/", "").trim();
                    bind.ivSecondStage.setVisibility(View.VISIBLE);
                    GlideClient.downloadSpriteImage(idTwo, bind.ivSecondStage);

                    // Check and set for the third evolution stage
                    if (secondStage.getNextEvolutions().length > 0) {
                        EvolutionChain thirdStage = secondStage.getNextEvolutions()[0];
                        String idThree = thirdStage.getSpecies().getUrl().substring(42).replace("/", "").trim();
                        bind.ivThirdStage.setVisibility(View.VISIBLE);
                        GlideClient.downloadSpriteImage(idThree, bind.ivThirdStage);
                    }
                }
            }

            @Override
            public void onFailure(Call<EvolutionChainLink> call, Throwable t) {
                Timber.d(t);
            }
        }, evolutionChainId);
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
                    bind.setPokemon(savedPokemon);
                    addTypeEffectiveness(xTwoFlexLayout, savedPokemon.getEffective());
                    addTypeEffectiveness(xHalfFlexLayout, savedPokemon.getNotEffective());
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
                            bind.setPokemon(savedPokemon);
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
                    }, bind.getPokemon().getTypeList()[1].getType().getName());//savedPokemon.getTypeList()[1].getType().getName());
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
