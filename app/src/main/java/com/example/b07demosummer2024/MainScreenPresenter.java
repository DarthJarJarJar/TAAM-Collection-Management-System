package com.example.b07demosummer2024;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainScreenPresenter implements MainScreenPresenterInterface {
    MainScreenView view;
    MainScreenModel model;

    int pageNumber;
    int maxPages;

    public MainScreenPresenter(MainScreenView view, MainScreenModel model) {
        this.view = view;
        this.model = model;
        this.model.setPresenterInterface(this);
        this.pageNumber = 1;
    }

    @Override
    public void update(int maxPages) {
        model.loadItemsFromDb();
        setMaxPages(maxPages);
        updatePage();
    }

    void loadItems() {
        if (model.areItemsLoaded()) {
            update((model.getItemList().size() + 9) / 10);
            updatePage();
        } else {
            model.loadItemsFromDb();
        }
    }

    void handlePageUp() {
        if (pageNumber < maxPages) {
            pageNumber++;
            updatePage();
        }
    }

    void handlePageDown() {
        if (pageNumber > 1) {
            pageNumber--;
            updatePage();
        }
    }

//    void ensureButtonBounds() {
//        view.setButtonBackEnableState(pageNumber > 1);
//        view.setButtonNextEnableState(pageNumber < maxPages);
//    }

    void paginateList() {
        List<Item> pagedList = new ArrayList<>();
        int start = (pageNumber - 1) * 10;
        int end = Math.min(start + 10, model.getItemList().size());

        for (int i = start; i < end; i++) {
            pagedList.add(model.getItemList().get(i));
        }

        view.updateRecyclerList(pagedList);
    }

    public void updatePage() {
        String pageInfo = makePageString();
        view.updatePageInfo(pageInfo);
        paginateList();
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }

    String makePageString() {
        return "Page " + pageNumber + " of " + maxPages;
    }


}
