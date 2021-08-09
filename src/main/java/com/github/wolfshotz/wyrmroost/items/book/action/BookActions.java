package com.github.wolfshotz.wyrmroost.items.book.action;

import java.util.ArrayList;
import java.util.List;

public class BookActions
{
    public static final List<BookAction> ACTIONS = new ArrayList<>();

    public static final BookAction DEFAULT = register(new DefaultBookAction());
    public static final BookAction SIT = register(new SitBookAction());
    public static final BookAction HOME = register(new HomeBookAction());
    public static final BookAction TARGET = register(new TargetBookAction());

    private static BookAction register(BookAction newAction)
    {
        ACTIONS.add(newAction);
        return newAction;
    }
}
