package com.nesine.poc.pages;

import com.nesine.poc.context.TestContext;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

// "Onay" modal of the site
public class ConfirmationDialog extends BasePage {

    private static final By DIALOG = By.id("mod-confirmation");
    private static final By MESSAGE = By.cssSelector("#mod-confirmation .modal-body");
    private static final By OK = By.id("mod-confirmation-ok");
    private static final By CANCEL = By.xpath("//*[@id='mod-confirmation']//button[normalize-space()='Vazgeç']");

    public ConfirmationDialog(TestContext context) {
        super(context);
    }

    public String message() {
        return visible(MESSAGE).getText().trim();
    }

    @Step("Answer 'Tamam' to the confirmation")
    public void accept() {
        answer(OK);
    }

    @Step("Answer 'Vazgeç' to the confirmation")
    public void dismiss() {
        answer(CANCEL);
    }

    private void answer(By button) {
        click(button);
        await().until(ExpectedConditions.invisibilityOfElementLocated(DIALOG));
    }
}
