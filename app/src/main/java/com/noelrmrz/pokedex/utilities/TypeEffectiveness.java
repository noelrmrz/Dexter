package com.noelrmrz.pokedex.utilities;

import com.noelrmrz.pokedex.pojo.Pokemon;
import com.noelrmrz.pokedex.pojo.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeEffectiveness {

    //create an object of SingleObject
    private static TypeEffectiveness instance = new TypeEffectiveness();
    private static String[] typeNames = new String[] {"ice",
            "water",
            "fire",
            "fighting",
            "flying",
            "normal",
            "rock",
            "ground",
            "steel",
            "dark",
            "psychic",
            "ghost",
            "poison",
            "bug",
            "bug",
            "fairy",
            "dragon",
            "electric"};

    private TypeEffectiveness() {

    }

    public static TypeEffectiveness getInstance() {
        return instance;
    }

    public static Pokemon setTypeEffectiveness(Type primaryType, Type secondaryType, Pokemon pokemon) {

        ArrayList<String> all = new ArrayList<>(Arrays.asList(typeNames));

        List<String> effective = getTypeNames(primaryType.getDamageRelations().getDoubleDamageFrom());
        List<String> notEffective = getTypeNames(primaryType.getDamageRelations().getHalfDamageFrom());
        List<String> immune = getTypeNames(primaryType.getDamageRelations().getNoDamageFrom());

        if (secondaryType != null) {
            // 4x damage
            List<String> superEffective = getTypeNames(primaryType.getDamageRelations().getDoubleDamageFrom());
            superEffective.retainAll(getTypeNames(secondaryType.getDamageRelations().getDoubleDamageFrom()));

            //Timber.d(getTypeNames(secondaryType.getDamageRelations().getDoubleDamageFrom()).toString());
            //Timber.d(getTypeNames(primaryType.getDamageRelations().getDoubleDamageFrom()).toString());
            // 2x damage;
            effective.addAll(getTypeNames(secondaryType.getDamageRelations().getDoubleDamageFrom()));

            // 1/2x damage
            notEffective.addAll(getTypeNames(secondaryType.getDamageRelations().getHalfDamageFrom()));

            // 1/4x damage
            List<String> notVeryEffective = getTypeNames(primaryType.getDamageRelations().getHalfDamageFrom());
            notVeryEffective.retainAll(getTypeNames(secondaryType.getDamageRelations().getHalfDamageFrom()));

            // no damage
            immune.addAll((getTypeNames(secondaryType.getDamageRelations().getNoDamageFrom())));

            // 1x damage.
            List<String> normal = getTypeNames(primaryType.getDamageRelations().getDoubleDamageFrom());
            normal.retainAll(getTypeNames(secondaryType.getDamageRelations().getHalfDamageFrom()));
            normal.addAll(getTypeNames(secondaryType.getDamageRelations().getDoubleDamageFrom()));
            normal.retainAll(getTypeNames(primaryType.getDamageRelations().getHalfDamageFrom()));

            // Remove any duplicate elements found in the other lists
            effective.removeAll(superEffective);
            effective.removeAll(normal);
            effective.removeAll(immune);
            effective.removeAll(notVeryEffective);
            effective.removeAll(notEffective);

            // Remove any duplicate elements found in the other lists
            notEffective.removeAll(notVeryEffective);
            notEffective.removeAll(normal);
            notEffective.removeAll(immune);
            notEffective.removeAll(effective);
            notEffective.removeAll(superEffective);

            // Remove duplicate elements from the master list. The remaining elements
            // will be added to the 1x damage list
            all.removeAll(superEffective);
            all.removeAll(effective);
            all.removeAll(notEffective);
            all.removeAll(notVeryEffective);
            all.removeAll(immune);

            // Clear the 1x damage list from previous elements.  Add remaining elements
            normal.clear();
            normal.addAll(all);

            pokemon.setSuperEffective(superEffective);
            pokemon.setEffective(effective);
            pokemon.setNormal(normal);
            pokemon.setNotEffective(notEffective);
            pokemon.setNotVeryEffective(notVeryEffective);
            pokemon.setImmune(immune);
        }
        else {
            all.removeAll(effective);
            all.removeAll(notEffective);
            all.removeAll(immune);

            pokemon.setEffective(effective);
            pokemon.setNotEffective(notEffective);
            pokemon.setImmune(immune);
            pokemon.setNormal(all);
        }

        return pokemon;
    }

    public static List<String> getTypeNames(Type[] typeArray) {
        ArrayList<String> names = new ArrayList<>();

        for (Type type : typeArray) {
            names.add(type.getName());
        }

        return names;
    }

}
