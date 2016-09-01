package com.losextraditables.krokodil.android.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.losextraditables.krokodil.R;
import com.losextraditables.krokodil.android.infrastructure.injector.component.ApplicationComponent;
import com.losextraditables.krokodil.android.infrastructure.injector.component.DaggerVideosComponent;
import com.losextraditables.krokodil.android.infrastructure.injector.module.ActivityModule;
import com.losextraditables.krokodil.android.infrastructure.injector.module.VideoModule;
import com.losextraditables.krokodil.android.models.SearchItemModel;
import com.losextraditables.krokodil.android.presenters.SearchVideosPresenter;
import com.losextraditables.krokodil.android.views.SearchVideosView;
import com.losextraditables.krokodil.android.views.renders.SearchItemRenderer;
import com.pedrogomez.renderers.ListAdapteeCollection;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;
import java.util.List;
import javax.inject.Inject;

public class SearchActivity extends BaseActivity implements SearchVideosView {

  @Inject SearchVideosPresenter presenter;
  @BindView(R.id.results_videos_list) RecyclerView resultVideos;

  private SearchView searchView;
  private RendererBuilder<SearchItemModel> rendererBuilder;
  private RVRendererAdapter<SearchItemModel> adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    ButterKnife.bind(this);

    presenter.initialize(this);

    ActionBar actionBar = this.getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(false);
      actionBar.setDisplayShowTitleEnabled(false);
    }

    resultVideos.setLayoutManager(new LinearLayoutManager(this));

    rendererBuilder = new RendererBuilder<SearchItemModel>()
        .withPrototype(new SearchItemRenderer())
        .bind(SearchItemModel
            .class, SearchItemRenderer.class);
  }

  @Override protected void initializeInjector(ApplicationComponent applicationComponent) {
    applicationComponent.inject(this);
    DaggerVideosComponent.builder()
        .applicationComponent(applicationComponent)
        .activityModule(new ActivityModule(this))
        .videoModule(new VideoModule()).build().inject(this);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.search, menu);
    MenuItem searchItem = menu.findItem(R.id.menu_search);
    createSearchView(searchItem);
    SearchView.SearchAutoComplete searchAutoComplete =
        (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
    searchAutoComplete.setHintTextColor(getResources().getColor(R.color.colorAccent));
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  private void createSearchView(MenuItem searchItem) {
    searchView = (SearchView) searchItem.getActionView();
    searchView.setVisibility(View.GONE);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String queryText) {
        presenter.search(queryText);
        return true;
      }

      @Override public boolean onQueryTextChange(String s) {
        return false;
      }
    });
    searchView.setQueryHint(getString(R.string.activity_find_song_hint));
    searchView.setIconifiedByDefault(false);
    searchView.setIconified(false);
  }

  @Override public void showVideos(List<SearchItemModel> videoModels) {
    ListAdapteeCollection<SearchItemModel> adapteeCollection = new ListAdapteeCollection<>(videoModels);
    adapter =
        new RVRendererAdapter<>(rendererBuilder, adapteeCollection);
    resultVideos.setAdapter(adapter);
  }

  @Override public void hideKeyboard() {
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
  }
}
