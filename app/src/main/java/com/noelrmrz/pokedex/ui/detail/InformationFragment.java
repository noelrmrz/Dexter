package com.noelrmrz.pokedex.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.flexbox.FlexboxLayout;
import com.noelrmrz.pokedex.R;
import com.noelrmrz.pokedex.databinding.FragmentInformationBinding;
import com.noelrmrz.pokedex.pojo.EvolutionChain;
import com.noelrmrz.pokedex.pojo.EvolutionChainLink;
import com.noelrmrz.pokedex.pojo.EvolutionDetail;
import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.pojo.Type;
import com.noelrmrz.pokedex.pojo.TypeLink;
import com.noelrmrz.pokedex.utilities.BindingAdapters;
import com.noelrmrz.pokedex.utilities.GlideClient;
import com.noelrmrz.pokedex.utilities.GsonClient;
import com.noelrmrz.pokedex.utilities.HelperTools;
import com.noelrmrz.pokedex.utilities.TypeHelper;
import com.noelrmrz.pokedex.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InformationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private final String LEVEL_UP = "level-up";
    private final String USE_ITEM = "use-item";
    private final String TRADE = "trade";
    static Pokemon savedPokemon;
    private final HelperTools helperTools = HelperTools.getInstance();
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

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

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

        createTypeEffectivenessView(bind.xOneFlexLayout, Arrays.asList(helperTools.getTypeNames()));

        setTypeMultipliers();

        String evolutionLink = savedPokemon.getPokemonSpecies().getEvolutionChainLink().getUrl().substring(41);
        String evolutionChainId = evolutionLink.replace("/", "").trim();

        mainViewModel.getEvolutionChainLinkMutableLiveData().observe(getViewLifecycleOwner(), new Observer<EvolutionChainLink>() {
            @Override
            public void onChanged(EvolutionChainLink evolutionChainLink) {
                // Set the image for the first evolution stage
                EvolutionChain firstStage = evolutionChainLink.getChain();
                String idOne = firstStage.getSpecies().getUrl().substring(42).replace("/", "").trim();
                GlideClient.downloadSpriteImage(idOne, bind.ivFirstStage);

                // Check and set the second evolution stage
                if (firstStage.getNextEvolutions().length > 0) {
                    EvolutionChain secondStage = firstStage.getNextEvolutions()[0];
                    String idTwo = secondStage.getSpecies().getUrl().substring(42).replace("/", "").trim();
                    bind.ivSecondStage.setVisibility(View.VISIBLE);
                    bind.tvFirstLevelUp.setVisibility((View.VISIBLE));
                    bind.ivFirstLevelUpArrow.setVisibility(View.VISIBLE);
                    setEvolutionText(bind.tvFirstLevelUp, secondStage.getEvolutionDetails()[0]);
                    GlideClient.downloadSpriteImage(idTwo, bind.ivSecondStage);

                    // Check and set for the third evolution stage
                    if (secondStage.getNextEvolutions().length > 0) {
                        EvolutionChain thirdStage = secondStage.getNextEvolutions()[0];
                        String idThree = thirdStage.getSpecies().getUrl().substring(42).replace("/", "").trim();
                        bind.ivThirdStage.setVisibility(View.VISIBLE);
                        bind.tvSecondLevelUp.setVisibility((View.VISIBLE));
                        bind.ivSecondLevelUpArrow.setVisibility(View.VISIBLE);
                        setEvolutionText(bind.tvSecondLevelUp, thirdStage.getEvolutionDetails()[0]);
                        GlideClient.downloadSpriteImage(idThree, bind.ivThirdStage);
                    }
                }
            }
        });
        mainViewModel.getPokemonEvolutionData(evolutionChainId);
    }

    private void createTypeEffectivenessView(FlexboxLayout layout, List<String> list) {
        for (int y = 0; y < list.size(); y++) {

            // Create a new instance of the view
            TypeCompoundView typeCompoundView = new TypeCompoundView(getContext());

            // Give the view an ID
            typeCompoundView.setId(HelperTools.getTypeId(getContext(), list.get(y)));

            // Set the type name
            typeCompoundView.setTypeName(list.get(y));

            // Set the effectiveness
            typeCompoundView.setTypeEffect(getString(R.string.one_x));

            // Set the background to correspond to the the type Color
            typeCompoundView.setValueColor(getContext().getResources().getColor(HelperTools
                    .getColor(getContext(), list.get(y))));

            // Add the view to the layout
            layout.addView(typeCompoundView);
        }
    }

    public List<TypeCompoundView> removeTypeEffectivenessView(FlexboxLayout layout, Type[] list) {
        List<TypeCompoundView> views = new ArrayList<>();

        for (Type type : list) {
            if (layout.findViewById(HelperTools.getTypeId(getContext(), type.getName())) != null) {
                // Find the TextView within the layout. Reset the text to just the type name.
                // Add the item to the list to return.
                TypeCompoundView textView = (TypeCompoundView) layout.findViewById(HelperTools.getTypeId(getContext(),
                        type.getName()));
                views.add(textView);

                layout.removeView(textView);
            }
        }
        return views;
    }

    public void addTypeEffectivenessView(FlexboxLayout layout, List<TypeCompoundView> views, String addition) {
        // Append the text modifications before adding the TextView to the layout
        for (TypeCompoundView view : views) {
            view.setTypeEffect(addition);
            layout.addView(view);
        }
    }

    private void setTypeMultipliers() {

        mainViewModel.getTypeMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Type>() {
            @Override
            public void onChanged(Type type) {

                // Get the primary and secondary Types from the ViewModel. Having issues with the
                // Callback method not getting executed for dual types
                Type primary = mainViewModel.getType(mainViewModel.getAllPokemonList().get(mainViewModel.position).getTypeList()[0].getType().getName());
                Type secondary = mainViewModel.getAllPokemonList().get(mainViewModel.position).getTypeList().length > 1 ? mainViewModel.getType(mainViewModel.getAllPokemonList().get(mainViewModel.position).getTypeList()[1].getType().getName()) : null;

                TypeHelper typeHelper = new TypeHelper(primary, secondary, mainViewModel.getAllPokemonList().get(mainViewModel.position).getTypeList().length);
                typeHelper.calculateTypeEffectivness();

                // No damage
                List<TypeCompoundView> textViews = removeTypeEffectivenessView(bind.xOneFlexLayout, typeHelper.getImmune().toArray(new Type[0]));
                addTypeEffectivenessView(bind.xHalfFlexLayout, textViews, getString(R.string.immune));

                // Super-effective
                textViews = removeTypeEffectivenessView(bind.xOneFlexLayout, typeHelper.getSuperEffective().toArray(new Type[0]));
                addTypeEffectivenessView(bind.xTwoFlexLayout, textViews, getString(R.string.four_x));

                // Effective
                textViews = removeTypeEffectivenessView(bind.xOneFlexLayout, typeHelper.getEffective().toArray(new Type[0]));
                addTypeEffectivenessView(bind.xTwoFlexLayout, textViews, getString(R.string.two_x));

                // Not-very-effective
                textViews = removeTypeEffectivenessView(bind.xOneFlexLayout, typeHelper.getNotVeryEffective().toArray(new Type[0]));
                addTypeEffectivenessView(bind.xHalfFlexLayout, textViews, getString(R.string.quarter_x));

                // Not-effective
                textViews = removeTypeEffectivenessView(bind.xOneFlexLayout, typeHelper.getNotEffective().toArray(new Type[0]));
                addTypeEffectivenessView(bind.xHalfFlexLayout, textViews, getString(R.string.half_x));
            }
        });

        for (TypeLink typeLink : mainViewModel.getAllPokemonList().get(mainViewModel.position).getTypeList()) {
            mainViewModel.getPokemonTypeData(typeLink.getType().getName());
        }
    }

    public void setEvolutionText(TextView textview, EvolutionDetail evolutionDetail) {
        switch(evolutionDetail.getTrigger().getName()){
            case(LEVEL_UP):
                if (evolutionDetail.getMinHappiness() != 0)
                    textview.setText(getString(R.string.happiness) + " " + evolutionDetail.getMinHappiness());
                else if(evolutionDetail.getMinBeauty() != 0)
                    textview.setText(getString(R.string.beauty) + " " + evolutionDetail.getMinBeauty());
                else if(evolutionDetail.getMinAffection() != 0)
                    textview.setText(getString(R.string.affection) + " " + evolutionDetail.getMinAffection());
                else if (!evolutionDetail.getTimeOfDay().equals(""))
                    textview.setText(evolutionDetail.getTimeOfDay());
                else
                    textview.setText(getString(R.string.level_up) + " " + evolutionDetail.getMinLevel());
                break;
            case(USE_ITEM):
                textview.setText(evolutionDetail.getItem().getName());
                break;
            case(TRADE):
                textview.setText(TRADE);
                break;
        }
    }
}
