package com.nesine.poc.steps;

import com.nesine.poc.config.ConfigReader;
import com.nesine.poc.pages.HeaderMenu;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class NavigationSteps {

    private final HeaderMenu headerMenu;

    public NavigationSteps(HeaderMenu headerMenu) {
        this.headerMenu = headerMenu;
    }

    @When("the user clicks the header link to {string}")
    public void clickHeaderLink(String path) {
        headerMenu.clickLinkTo(path);
    }

    @When("the user refreshes the page")
    public void refreshPage() {
        headerMenu.refresh();
    }

    // query is ignored, /iddaa opens as /iddaa?et=1&le=2
    @Then("the {string} page should be open with a title containing {string}")
    public void pageShouldBeOpen(String path, String titlePart) {
        URI actual = URI.create(headerMenu.currentUrl());
        assertThat(actual.getHost()).isEqualTo(URI.create(ConfigReader.get("base.url")).getHost());
        assertThat(actual.getPath()).isEqualTo(path);
        assertThat(headerMenu.title()).contains(titlePart);
    }
}
