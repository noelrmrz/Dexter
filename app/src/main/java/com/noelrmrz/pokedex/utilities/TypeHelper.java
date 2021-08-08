package com.noelrmrz.pokedex.utilities;

import com.noelrmrz.pokedex.pojo.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeHelper {

    private Type primary;
    private Type secondary;
    private List<Type> superEffective;
    private List<Type> effective;
    private List<Type> normal;
    private List<Type> notEffective;
    private List<Type> notVeryEffective;
    private List<Type> immune;
    private int count;

    public TypeHelper(Type primary, Type secondary, int count) {
        this.primary = primary;
        this.secondary = secondary;
        this.count = count;
        superEffective = new ArrayList<>();
        effective = new ArrayList<>();
        normal = new ArrayList<>();
        notEffective = new ArrayList<>();
        notVeryEffective = new ArrayList<>();
        immune = new ArrayList<>();
    }

    public void calculateTypeEffectivness() {
        if (primary != null && secondary != null) {
            addCommonTypes(superEffective, effective, primary.getDamageRelations().getDoubleDamageFrom(),
                    secondary.getDamageRelations().getDoubleDamageFrom());
            addCommonTypes(notVeryEffective, notEffective, primary.getDamageRelations().getHalfDamageFrom(),
                    secondary.getDamageRelations().getHalfDamageFrom());
            addImmuneTypes(immune, primary.getDamageRelations().getNoDamageFrom(),
                    secondary.getDamageRelations().getNoDamageFrom());
            // Removes duplicates
            removeDuplicateTypes(effective, notEffective);
        } else if (count == 1) {
            addMonoTypes(primary);
        }
        else {
            // Do nothing wait for primary and secondary to finish loading
        }
    }

    private void addCommonTypes(List<Type> stackOne, List<Type> stackTwo, Type[] primaryTypeArray,
                                Type[] secondaryTypeArray) {
        // Iterate over both arrays to find common Types and add to the ArrayList
        // Special case for normal dual types
        if (primaryTypeArray.length == 0) {
            stackTwo.addAll(Arrays.asList(secondaryTypeArray));
        } else {
            for (Type primary : primaryTypeArray) {
                // Push each type into the second stack by default
                stackTwo.add(primary);
                for (Type secondary : secondaryTypeArray) {
                    // If its a common Type push onto the first stack and pop from the first stack
                    if (primary.getName().equalsIgnoreCase(secondary.getName())) {
                        stackOne.add(primary);
                        stackTwo.remove(primary);
                    } else if (!stackTwo.contains(secondary)) {
                        stackTwo.add(secondary);
                    }
                }
            }
        }
    }

    private void addImmuneTypes(List<Type> stackOne, Type[] primaryTypeArray, Type[] secondaryTypeArray) {
        // Add all immunities
        stackOne.addAll(Arrays.asList(primaryTypeArray));
        stackOne.addAll(Arrays.asList(secondaryTypeArray));
    }

    private void addMonoTypes(Type primary) {
        // Monotype
        effective = Arrays.asList(primary.getDamageRelations().getDoubleDamageFrom());
        notEffective = Arrays.asList(primary.getDamageRelations().getHalfDamageFrom());
        immune = Arrays.asList(primary.getDamageRelations().getNoDamageFrom());
    }

    private void removeDuplicateTypes(List<Type> stackOne, List<Type> stackTwo) {
        List<Type> temp = new ArrayList<>();
        List<Type> temp2 = new ArrayList<>();

        // Iterate through the effective elements and remove any items that cancel out
        for (Type type : stackOne) {
            for (Type typeArrayItem: stackTwo) {
                if (type.getName().equalsIgnoreCase(typeArrayItem.getName())) {
                    temp.add(type);
                    temp2.add(typeArrayItem);
                }
            }
        }

        stackOne.removeAll(temp);
        stackTwo.removeAll(temp2);
    }

    public List<Type> getSuperEffective() {
        return superEffective;
    }

    public List<Type> getEffective() {
        return effective;
    }

    public List<Type> getNotVeryEffective() {
        return notVeryEffective;
    }

    public List<Type> getNotEffective() {
        return notEffective;
    }

    public List<Type> getImmune() {
        return immune;
    }
}
