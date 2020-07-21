
import java.util.List;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class Bot_Files {
	
public static void main(String[] args) throws InterruptedException {
	
		//Be sure you have an account to use for the bot
		String whoToChatWith = "Username";//Username/group chat name for who you'd like the bot to interact with
		String botUsername = "Username";//Username for the bot
		String botPassword = "Password";//Password for the bot
		
		//Loads up instagram
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\thana\\Documents\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.instagram.com");
		System.out.println(driver.getTitle());
		System.out.println(driver.getCurrentUrl()+"\n");
		
		//Logs in bot
		WebDriverWait wait1 = new WebDriverWait(driver, 1);
		WebDriverWait wait = new WebDriverWait(driver, 5);
		WebDriverWait wait10 = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='username']"))).sendKeys(botUsername);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='password']"))).sendKeys(botPassword);
		driver.findElement(By.xpath("//div[text()='Log In']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("_8-yf5")));
		
		//Goes to the bot's DMs
		driver.get("https://www.instagram.com/direct/inbox/");
		System.out.println(driver.getTitle());
		System.out.println(driver.getCurrentUrl());
		
		//Opens up DM with the user or group chat the bot is meant to chat with
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Not Now']")));
		driver.findElement(By.xpath("//button[text()='Not Now']")).click();
		try {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='"+whoToChatWith+"']")));
		driver.findElement(By.xpath("//div[text()='"+whoToChatWith+"']")).click();
		}catch (NoSuchElementException e ) {
			System.out.println("\nNo such user exists");
			System.exit(0);
		}catch(TimeoutException e) {
			System.out.println("\nNo such user exists");
			System.exit(0);
		}
		
		//Holds texts to make sure the bot doesn't repeat actions
		String Same = "Test if it's the same text";
		String temp = "placeholder";
		String text = "placeholder";
		
		//Sends that the bot is online
		sendText("Bot online",driver);
		
		while(!text.equals("!exit")) {
			try{
			//Analyzes the latest texts
			List<WebElement> listOfElements = driver.findElements(By.xpath("//div[@class = '_7UhW9   xLCgt      MMzan  KV-D4            p1tLr      hjZTB']"));
			
			//Strips html text from the texts
			text = listOfElements.get(listOfElements.size()-1).getAttribute("innerHTML");
			String text2 = text.replace("<span>","");
		    text = text2.replace("</span>","");
		   
		    //All commands to the bot must start with a '!'
		    if (!text.equals(Same)&&(text.charAt(0)=='!')&&!text.equals("!exit")) {
		    	
		    	//Will respond to '!hello' with with hello
		    	//Best used for testing bot response
		    	if (text.contains("!Hello")||text.contains("!hello")) {
		    	sendText("Hello",driver);
		    	}
		    	//Will display all commands available, feel free to add on if you make new commands
		    	else if (text.contains("!guide")) {
			    	sendText("Welcome to doggo.bot!\r\n" + 
			    			"!hello - say hi back\r\n" + 
			    			
			    			"!weather - fetches wather for certain location\r\n" + 
			    			"!yt - first result youtube\r\n" +
			    			"!img - first result google images (doesn't work right now)\r\n" + 
			    			"!text - text someone through the bot\r\n" + 
			    			"!chat - chat with bot, powered by Elbot\r\n" + 
			    			"!post - gets latest post of public instagram user\r\n" + 
			    			"!exit - closes bot",driver);
			    	}
		    	// Get's weather of any location after '!weather'
		    	else if (text.contains("!weather")) {
		    		try{
		    		String location = "placeholder";
		    		
		    		//Subtracts '!weather' from string
		    		location = text.substring(9);
		    		
		    		//Gets temperature, weather, and exact string for location
		    		driver.get("https://www.google.com/");	
		    		driver.findElement(By.xpath("//*[@id=\"tsf\"]/div[2]/div[1]/div[1]/div/div[2]/input")).sendKeys("What's the weather in "+location,Keys.RETURN);
		    		location = driver.findElement(By.xpath("//*[@id=\"wob_loc\"]")).getText();
		    		temp = driver.findElement(By.xpath("//*[@id=\"wob_tm\"]")).getText();
		    		temp += "°C with " + driver.findElement(By.xpath("//*[@id=\"wob_dc\"]")).getText().toLowerCase() + " weather";
		    		driver.navigate().back();
		    		
		    		//Sends text
		    		goBack(whoToChatWith,driver,wait);
		    		sendText("Temperature in " + location + " is currently " + temp,driver);}
		    		catch(StringIndexOutOfBoundsException e) {
		    			sendText("Please something string after !yt", driver);
		    		}catch (NoSuchElementException e) {
		    			driver.navigate().back();
			    		goBack(whoToChatWith,driver,wait);
			    		sendText("No temperature can be fetched", driver);
		    		}
		    	}
		    	
		    	//Gets first result for search on youtube
		    	else if (text.contains("!yt")) {
		    		try{
		    		//Retrieves link for video (chance of retrieving ad link by mistake)
		    		driver.get("https://www.youtube.com/results?search_query="+text.substring(4,text.length()));
		    		Thread.sleep(2000);
		    		List<WebElement> youtubeVids =  driver.findElements(By.xpath("//*[@id=\"contents\"]/ytd-video-renderer[1]"));
		    		temp = youtubeVids.get(youtubeVids.size()-1).findElement(By.xpath("//*[@id=\"contents\"]/ytd-video-renderer[1]")).getAttribute("href");
		    		System.out.println(temp);
		    		if (temp.contains("www.googleadservices.com")) {
		    			temp = youtubeVids.get(youtubeVids.size()-2).findElement(By.xpath("/html/body/ytd-app/div/ytd-page-manager/ytd-search/div[1]/ytd-two-column-search-results-renderer/div/ytd-section-list-renderer/div[2]/ytd-item-section-renderer/div[3]/ytd-search-pyv-renderer/div/ytd-promoted-video-renderer/ytd-thumbnail/a")).getAttribute("href");
		    			System.out.println(temp);
		    		}
		    		//Sends link
		    		goBack(whoToChatWith,driver,wait);
		    		sendText(temp,driver);
		    		}
		    		//In case there is no string after '!yt'
		    		catch(StringIndexOutOfBoundsException e) {
		    			sendText("Please something string after !yt", driver);
		    		}
		    		//By.linkText(")
		    	}
		    	//Sends first result for the string after '!img'
		    	else if (text.contains("!img")) {
		    		try {
		    			
		    		//retrieves image link
		    		driver.get("https://www.google.ca/imghp?hl=en&tab=wi&ogbl");
		    		driver.findElement(By.xpath("//input[@name = 'q']")).sendKeys(text.substring(5,text.length()));
		    		driver.findElement(By.xpath("//button[@type = 'submit']")).click();
		    		driver.findElement(By.xpath("//img[@jsname='Q4LuWd']")).click();
		    		wait10.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/c-wiz/div[3]/div[2]/div[3]/div/div/div[3]/div[2]/c-wiz/div[1]/div[1]/div/div[2]/a/img")));
		    		Thread.sleep(1000);
		    		temp = driver.findElement(By.xpath("/html/body/div[2]/c-wiz/div[3]/div[2]/div[3]/div/div/div[3]/div[2]/c-wiz/div[1]/div[1]/div/div[2]/a/img")).getAttribute("src");
		    		System.out.println(temp);
		    		driver.navigate().back();
		    		driver.navigate().back();
		    		
		    		//goes back and sends text
		    		goBack(whoToChatWith,driver,wait);
		    		sendText(temp,driver);}
		    		
		    		//In case person does not attach string after '!img'
		    		catch(StringIndexOutOfBoundsException e) {
		    			goBack(whoToChatWith,driver,wait);
		    			sendText("Please type something after !img", driver);
		    		}

		    	}
		    	//Sends text to what user is typed after '!text'
		    	//Cannot recieve texts yet when chat is opened
		    	else if (text.contains("!text")) {
		    		try {
		    		
		    		//Subtracts '!text' from string
		    		String Reciever = text.substring(6);
		    		
		    		//Searches user and opens DM
		    		driver.findElement(By.xpath("//button[@type = 'button']")).click();
		    		driver.findElement(By.xpath("//input[@placeholder = 'Search...']")).sendKeys(Reciever);
		    		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class = 'dCJp8 ']")));
		    		driver.findElement(By.xpath("//button[@class = 'dCJp8 ']")).click();
		    		driver.findElement(By.xpath("//div[@class = 'rIacr']")).click();
		    		
		    		//Tells person that's receiving the texts that they are chatting with the bot
		    		//Feel free to comment this block out if you don't want to use it
		    		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@placeholder = 'Message...']")));
		    		sendText("You are now starting a chat with the Insta Discord bot",driver);
			    	driver.findElement(By.xpath("//div[text()='"+whoToChatWith+"']")).click();
		    		
			    	//Tells you that the chat has started
			    	sendText("Chat has started\nAll sentences with a \"!\" in front will be sent",driver);
			    	
			    	//Variables to ensure no actions are repeated
			    	String Same2 = "Sees if text is the same";
			    	String t = "placeholder";
			    	String lastText = "placeholder";
			    	
			    	while(!t.equals("!exit")) {
						try{
						//Analyzes your latest texts
						//ALL TEXTS MUST HAVE A '!' IN THE BEGGINING IN ORDER TO BE SENT
						List<WebElement> Senders = driver.findElements(By.xpath("//div[@class = '_7UhW9   xLCgt      MMzan  KV-D4            p1tLr      hjZTB']"));
						t = Senders.get(Senders.size() - 1).getAttribute("innerHTML");
						String t2 = t.replace("<span>","");
					    t = t2.replace("</span>","");
					    
					    if (!t.equals(Same2)&&t.charAt(0)=='!'&&!t.equals("!exit")) {
					    	
					    	//Sends string after '!' to the receiver
					    	driver.findElement(By.xpath("//div[text()='"+Reciever+"']")).click();
					    	sendText(t.substring(1,t.length()),driver);
					    	
					    	//I plan to use this chunk when I upgrade the bot to receive texts from the receiver
					    	/*List<WebElement> Recievers = driver.findElements(By.xpath("//div[@class = '    CMoMH    _8_yLp  ']"));
							lastText = Recievers.get(Recievers.size() - 1).getAttribute("innerHTML");
							t2 = t.replace("<span>","");
							lastText = t2.replace("</span>","");*/
					    	
					    	//Confirms to you that the text has been sent
					    	driver.findElement(By.xpath("//div[text()='"+whoToChatWith+"']")).click();
					    	sendText("\""+t.substring(1,t.length())+"\" has been sent to " + Reciever, driver);
					    			//+"\n Last text received: " + lastText
							    	
					    }
		    		
						}catch(org.openqa.selenium.StaleElementReferenceException ex) {}
				    }
			    	//Tell's you that the chat has been exited
			    	sendText("Chat exited", driver);}
		    		catch(StringIndexOutOfBoundsException e) {
		    			//In case no username is put after '!text'
		    			driver.navigate().back();
		    			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='"+whoToChatWith+"']")));
		    			driver.findElement(By.xpath("//div[text()='"+whoToChatWith+"']")).click();
		    			sendText("Please type something after !text", driver);
		    		}
		    	}
		    	//Starts chat with ELbot, an online chatbot
		    	//No strings are required after '!chat'
		    	else if (text.contains("!chat")) {
		    		//Opens new window
		    		System.setProperty("webdriver.chrome.driver", "C:\\Users\\thana\\Documents\\chromedriver.exe");
		    		WebDriver second_driver = new ChromeDriver();
		    		WebDriverWait wait2 = new WebDriverWait(second_driver, 5);
		    		//Retrieves Elbot's opening statement
		    		second_driver.get("http://elbot-e.artificial-solutions.com/cgi-bin/elbot.cgi");
		    		temp = second_driver.findElement(By.xpath("/html/body/form/table/tbody/tr[3]/td[2]")).getText();
		    		System.out.println(temp);
		    		
		    		Set<String> allHandles = driver.getWindowHandles();

		    	    //count the handles Here count is=2
		    	    System.out.println("Count of windows:"+allHandles.size());      

		    	    //Get current handle or default handle
		    	    String currentWindowHandle = allHandles.iterator().next();
		    	    System.out.println("currentWindow Handle"+currentWindowHandle);

		    	    //get the last Window Handle
		    	    String lastHandle = allHandles.iterator().next();
		    	    System.out.println("last window handle"+lastHandle);

		    	    //switch to second/last window, because we know there are only two windows 1-parent window 2-other window(ad window)
		    	    driver.switchTo().window(lastHandle);
		    	    System.out.println(driver.getTitle());
		    	    
		    	    //Tells you that chat has been started and sends Elbot's opening statement
		    	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@placeholder = 'Message...']")));
		    	    sendText("Chat has started\nAll sentences with a \"!\" in front will be sent",driver);
		    		sendText(temp, driver);
		    		
		    		//Variables to prevent bot from performing repeat actions
		    		String Same2 = "Sees if text is the same";
			    	String t = "placeholder";
			    	
		    		while(!t.equals("!exit")) {
						try{
						//Looks at your latest texts and sends new ones to Elbot
						//ONLY TEXTS THAT START WITH '!' WILL BE SENT TO ELBOT
						List<WebElement> Senders = driver.findElements(By.xpath("//div[@class = '_7UhW9   xLCgt      MMzan  KV-D4            p1tLr      hjZTB']"));
						t = Senders.get(Senders.size() - 1).getAttribute("innerHTML");
						String t2 = t.replace("<span>","");
					    t = t2.replace("</span>","");
					    if (!t.equals(Same2)&&t.charAt(0)=='!'&&!t.equals("!exit")) {
					    	Same2 = t;
					    	//Opens up new window and sends your text to Elbot
					    	driver.switchTo().window(currentWindowHandle);
					    	System.out.println(driver.getTitle());
					    	second_driver.findElement(By.xpath("/html/body/form/table/tbody/tr[4]/td[2]/input")).sendKeys(t.substring(1),Keys.RETURN);
					    	wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/form/table/tbody/tr[4]/td[2]/input")));
					    	temp = second_driver.findElement(By.xpath("/html/body/form/table/tbody/tr[3]/td[2]")).getText();
					    	System.out.println(temp);
					    	//Sends Elbot's response back to you
					    	driver.switchTo().window(lastHandle);
					    	sendText(temp,driver);
					    	
					    }
		    		
						}catch(org.openqa.selenium.StaleElementReferenceException ex) {}
				    }
		    		//Closes Elbot's window
		    		//Tells you that the connection to Elbot has been closed
		    		second_driver.close();
			    	sendText("Chat exited", driver);
		    		
		    	}
		    	//Retrieves latest post of whatever user is put after '!post'
		    	//I plan to update this so it sends the post directly to you instead of the link
		    	else if (text.contains("!post")) {
		    		//Strips '!post' from string and goes to the user's page
		    		String Page = text.substring(6);
		    		driver.get("https://www.instagram.com/"+Page+"/");
		    		try {

		    		//Finds link for latest post
		    		temp = driver.findElement(By.xpath("//img[@class='FFVAD']")).getAttribute("src");
		    		System.out.println(temp);
		    		goBack(whoToChatWith,driver,wait);
		    		sendText(temp, driver);}
		    		//If page is private or has no posts the bot will return this
		    		catch (NoSuchElementException e) {
		    			goBack(whoToChatWith,driver,wait);
		    			sendText("Page is either private or has no posts", driver);
		    		}
		    	}
		    	//If the bot cannot recognize the command, it will tell them so and will tell them to use !guide for all commands
		    	else {
		    		sendText("Hmm..not sure what you said\nuse !guide to see all commands",driver);
		    	}
		    	
		    	driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		    }}catch(org.openqa.selenium.StaleElementReferenceException ex) {}
		    }
		
		//If the you type '!exit' the bot will close
		sendText("Aight, cya",driver);
		driver.close();
		
		}

//Sends text
//Must have DM open
public static void sendText (String text, WebDriver driver){
	System.out.println(text);
	driver.findElement(By.xpath("//textarea[@placeholder = 'Message...']")).sendKeys(text);
	driver.findElement(By.xpath("//button[text()='Send']")).click();
}
//Navigates back and opens DM back up
public static void goBack (String DMplace, WebDriver driver, WebDriverWait wait) {
	driver.navigate().back();
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='"+DMplace+"']")));
	driver.findElement(By.xpath("//div[text()='"+DMplace+"']")).click();
}

}
