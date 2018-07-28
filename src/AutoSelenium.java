import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class AutoSelenium {
    public static void main(String[] args) {
        // 1. Open Autoplius
        ChromeDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://autoplius.lt/");

        // 2. Selecting Make from DropDown
        Select clickAudiMake = new Select(driver.findElement(By.id("make_id")));
        clickAudiMake.selectByVisibleText("Audi");

        // 3. Selecting Model from dropDown
        Select clickAudiModel = new Select(driver.findElement(By.id("model_id")));
        clickAudiModel.selectByVisibleText("A6");

        // 4. Unchecking checkbox of "New"
        var newCheckBox = driver.findElementByXPath("//*[@id=\"fast_search_forms\"]/div[1]/div[2]/label");
        if (newCheckBox.isDisplayed()) {
            newCheckBox.click();
        }

        // 5. Input "geras" into text field
        var inputWordToTextField = driver.findElementById("qt");
        inputWordToTextField.sendKeys("geras");

        // 6. Search
        var searchButton = driver.findElement(By.xpath("//a[@class='submit-link submit-link-new submit-red fr']"));
        searchButton.click();

        // 7.1. Checking: How many results are displayed
        var resultCheck = driver.findElement(By.className("result-count")).getText().replaceAll("[()]", "");
        int resultCheckStringToInt = Integer.parseInt(resultCheck);
        System.out.println("Displayed result: " + resultCheckStringToInt);


        // 7.2. Checking: If all parameters where involved into search.
        var filters = driver.findElement(By.xpath("//div[@class=\"facets-list\"]/ul"));
        List<WebElement> filtersList = filters.findElements(By.tagName("li"));
        for (WebElement li : filtersList) {
            if (li.getText().equals("Tekstinė paieška: geras")) {
                System.out.println("'geras' exist");
            } else if (li.getText().equals("Naudoti")) {
                System.out.println("'Naudoti' exist");
            } else if (li.getText().equals("Audi A6")) {
                System.out.println("'Audi A6' exist");
            }
        }

        // 8. Remove text input "geras"
        var deleteTextInput = driver.findElementByXPath("//li[contains (., 'geras')]");
        deleteTextInput.click();
        WebElement textInput = driver.findElement(By.xpath("//li[contains (., \"geras\")]/a"));
        Actions action = new Actions(driver);
        action.moveToElement(textInput).build().perform();
        driver.findElement(By.xpath("//li[contains (., \"geras\")]//li/a")).click();

        // 9. Compare results before with input and without input
        var resultWithoutInput = driver.findElement(By.className("result-count")).getText().replaceAll("[()]", "");
        int resultWithoutInputStringToInt = Integer.parseInt(resultWithoutInput);
        System.out.println("Displayed result: " + resultWithoutInputStringToInt);
        if (resultCheckStringToInt < resultWithoutInputStringToInt) {
            System.out.println("More results displayed without text input 'geras' : " + resultCheckStringToInt + " < " + resultWithoutInput);
        } else if (resultCheckStringToInt < resultWithoutInputStringToInt) {
            System.out.println("Same amount of results displayed : " + resultCheckStringToInt + " = " + resultWithoutInput);
        } else {
            System.out.println("More results displayed with text input 'geras' : " + resultCheckStringToInt + " > " + resultWithoutInput);
        }

        // 10. Edit search and select additional car "A6 Allroad"
        var editSearch = driver.findElement(By.className("search-edit-link"));
        editSearch.click();
        Select addNewCarModel = new Select(driver.findElement(By.id("make_id_sublist_portal")));
        addNewCarModel.selectByVisibleText("A6 ALLROAD");
        var AddButton = driver.findElementById("addbtn_make_id");
        AddButton.click();

        // 11. Search again and click on slider Notifications via e-mail
        var renewSearch = driver.findElement(By.className("ico-search"));
        renewSearch.click();
        var emailNotifications = driver.findElementByCssSelector("div.email-subscription-control >  div span.slider");
        emailNotifications.click();

        // 12. Without any input try to subscribe, and check if error message is displayed.
        driver.switchTo().activeElement();
        WebDriverWait waitForElement = new WebDriverWait(driver, 10);
        WebElement subscribeButton = waitForElement.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("div.dialog.email-subscribe-dialog > div button"))
        );

        subscribeButton.click();
        var errorMessage = driver.findElement(By.cssSelector("div.dialog.email-subscribe-dialog > div  div.subscription-email.has-error > div"));
            if(errorMessage.isDisplayed()){
                System.out.println("Error message displayed: " + errorMessage.getText());
            } else {
                System.out.println("Error message not displayed");
            }

    }
}
