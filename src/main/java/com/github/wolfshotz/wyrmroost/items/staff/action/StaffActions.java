package com.github.wolfshotz.wyrmroost.items.staff.action;

import java.util.ArrayList;
import java.util.List;

public class StaffActions
{
    public static final List<StaffAction> ACTIONS = new ArrayList<>();

    public static final StaffAction DEFAULT = register(new OpenUIStaffAction());
    public static final StaffAction INVENTORY = register(new InventoryStaffAction());
    public static final StaffAction SIT = register(new SitStaffAction());
    public static final StaffAction HOME = register(new HomeStaffAction());
    public static final StaffAction TARGET = register(new TargetStaffAction());

    private static StaffAction register(StaffAction newAction)
    {
        ACTIONS.add(newAction);
        return newAction;
    }
}
