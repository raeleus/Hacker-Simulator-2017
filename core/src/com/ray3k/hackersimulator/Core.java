package com.ray3k.hackersimulator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Core extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Window loginWindow;
    private Window bruteForceWindow;
    private Window vulnerabilityWindow;
    private Window sendMailWindow;
    private Window readMailWindow;
    private Window readIndividualMailWindow;
    private Window accountWindow;
    private ObjectMap<String, String> nameToPasswords;
    private ObjectMap<String, Boolean> canBeBFlist;
    private ObjectMap<String, Boolean> canBeVBlist;
    private ObjectMap<String, Boolean> canBePhishedList;
    private ObjectMap<String, Boolean> canBeKeyLoggedList;
    private ObjectMap<String, String> ipToNames;
    private ObjectMap<String, String> emailToNames;
    private Array<GeneratedMail> generatedMails;
    private Table popupTable;

    @Override
    public void create() {
        generatedMails = new Array<GeneratedMail>();
        nameToPasswords = new ObjectMap<String, String>();
        nameToPasswords.put("larry", "$rtF3Z");
        nameToPasswords.put("rickard", "qwerty1234!");
        nameToPasswords.put("steph", "Bananas1");
        nameToPasswords.put("ruby", "password2");
        nameToPasswords.put("root", "%fve5zeT@");
        
        ipToNames = new ObjectMap<String, String>();
        ipToNames.put("192.168.1.103", "larry");
        ipToNames.put("192.168.1.104", "rickard");
        ipToNames.put("192.168.1.105", "steph");
        ipToNames.put("192.168.1.106", "ruby");
        ipToNames.put("192.168.1.1", "root");
        
        emailToNames = new ObjectMap<String, String>();
        emailToNames.put("larry@acme.com", "larry");
        emailToNames.put("rickard@acme.com", "rickard");
        emailToNames.put("steph@acme.com", "steph");
        emailToNames.put("ruby@acme.com", "ruby");
        emailToNames.put("root@acme.com", "root");
        
        canBeBFlist = new ObjectMap<String, Boolean>();
        canBeBFlist.put("larry", false);
        canBeBFlist.put("rickard", false);
        canBeBFlist.put("steph", false);
        canBeBFlist.put("ruby", true);
        canBeBFlist.put("root", false);
        
        canBeVBlist = new ObjectMap<String, Boolean>();
        canBeVBlist.put("larry", false);
        canBeVBlist.put("rickard", true);
        canBeVBlist.put("steph", false);
        canBeVBlist.put("ruby", false);
        canBeVBlist.put("root", false);
        
        canBeKeyLoggedList = new ObjectMap<String, Boolean>();
        canBeKeyLoggedList.put("larry", true);
        canBeKeyLoggedList.put("rickard", false);
        canBeKeyLoggedList.put("steph", false);
        canBeKeyLoggedList.put("ruby", false);
        canBeKeyLoggedList.put("root", false);
        
        canBePhishedList = new ObjectMap<String, Boolean>();
        canBePhishedList.put("larry", false);
        canBePhishedList.put("rickard", false);
        canBePhishedList.put("steph", true);
        canBePhishedList.put("ruby", false);
        canBePhishedList.put("root", false);
        
        skin = new Skin(Gdx.files.local("hacker_simulator_data/hacker-simulator-2017.json"));
        skin.get("markup", LabelStyle.class).font.getData().markupEnabled = true;
        
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        
        Table table = new Table();
        root.add(table).expand().top().left().pad(10.0f);
        
        table.defaults().space(20.0f).width(100.0f);
        DesktopButton desktopButton = new DesktopButton("mail", skin);
        table.add(desktopButton);
        
        desktopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (readMailWindow == null) {
                    showReadMail("hacker");
                } else {
                    readMailWindow.remove();
                    showReadMail("hacker");
                }
            }
        });
        
        desktopButton = new DesktopButton("send-mail", skin);
        table.add(desktopButton);
        
        desktopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (sendMailWindow == null) {
                    showSendMail();
                } else {
                    sendMailWindow.remove();
                    showSendMail();
                }
            }
        });
        
        desktopButton = new DesktopButton("log-in", skin);
        table.add(desktopButton);
        
        desktopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (loginWindow == null) {
                    showLogIn();
                } else {
                    loginWindow.toFront();
                }
            }
        });
        
        table.row();
        desktopButton = new DesktopButton("brute-force", skin);
        table.add(desktopButton);
        
        desktopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (bruteForceWindow == null) {
                    showBruteForce();
                } else {
                    bruteForceWindow.toFront();
                }
            }
        });
        
        desktopButton = new DesktopButton("vulnerability", skin);
        table.add(desktopButton);
        
        desktopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (vulnerabilityWindow == null) {
                    showVulnerability();
                } else {
                    vulnerabilityWindow.toFront();
                }
            }
        });
        
        popupTable = new Table(skin);
        popupTable.setBackground("button");
        popupTable.setWidth(100.0f);
        popupTable.setHeight(50.0f);
        popupTable.setPosition(Gdx.graphics.getWidth(), 0.0f, Align.topRight);
        stage.addActor(popupTable);
        
        Label label = new Label("New Email", skin);
        popupTable.add(label);
    }
    
    private void popNotification() {
        popupTable.toFront();
        popupTable.addAction(Actions.sequence(Actions.moveToAligned(Gdx.graphics.getWidth(), 0, Align.bottomRight, 1.0f, Interpolation.circle), Actions.delay(1.0f), Actions.moveToAligned(Gdx.graphics.getWidth(), 0, Align.topRight, 1.0f, Interpolation.circle)));
    }
    
    private void showLogIn() {
        loginWindow = new Window("Log into account", skin);
        loginWindow.getTitleLabel().setStyle(skin.get("window-title", LabelStyle.class));
        loginWindow.getTitleTable().getCells().first().fill(false);
        stage.addActor(loginWindow);
        loginWindow.setSize(400.0f, 400.0f);
        loginWindow.setPosition(MathUtils.random(0, Gdx.graphics.getWidth() - (int) loginWindow.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight() - (int) loginWindow.getHeight()), Align.bottomLeft);
        
        Table table = new Table();
        loginWindow.add(table).grow().pad(20.0f);
        
        table.defaults().left().colspan(3);
        Label label = new Label("Server", skin, "server");
        table.add(label);
        
        table.row();
        final ButtonGroup buttonGroup = new ButtonGroup();
        TextButton button = new TextButton("Larry @ 192.168.1.103", skin, "list");
        button.setUserObject("larry");
        buttonGroup.add(button);
        table.add(button);
        
        table.row();
        button = new TextButton("Rickard @ 192.168.1.104", skin, "list");
        button.setUserObject("rickard");
        buttonGroup.add(button);
        table.add(button);
        
        table.row();
        button = new TextButton("$t3ph @ 192.168.1.105", skin, "list");
        button.setUserObject("steph");
        buttonGroup.add(button);
        table.add(button);
        
        table.row();
        button = new TextButton("Ruby_Rulz @ 192.168.1.106", skin, "list");
        button.setUserObject("ruby");
        buttonGroup.add(button);
        table.add(button);
        
        table.row();
        button = new TextButton("root @ 192.168.1.1", skin, "list");
        button.setUserObject("root");
        buttonGroup.add(button);
        table.add(button);
        
        table.defaults().padTop(15.0f).colspan(1).space(5.0f);
        table.row();
        label = new Label("Password: ", skin);
        table.add(label);
        
        final TextField textField = new TextField("", skin);
        table.add(textField);
        stage.setKeyboardFocus(textField);
        
        TextButton textButton = new TextButton("Log In", skin, "small");
        table.add(textButton);
        
        table.defaults().colspan(3);
        table.row();
        final Label loadingLabel = new Label("", skin);
        loadingLabel.setWrap(true);
        loadingLabel.setAlignment(Align.topLeft);
        table.add(loadingLabel).grow();
        
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                final String name = (String) buttonGroup.getChecked().getUserObject();
                if (checkPassword(name, textField.getText())) {
                    loadingLabel.setText("Signing in...");
                    loadingLabel.addAction(Actions.delay(2.0f, new Action() {
                        @Override
                        public boolean act(float delta) {
                            loadingLabel.setText("Success");
                            if (accountWindow != null) {
                                accountWindow.remove();
                            }
                            showAccount(name);
                            return true;
                        }
                    }));
                } else {
                    loadingLabel.setText("Signing in...");
                    loadingLabel.addAction(Actions.delay(2.0f, new Action() {
                        @Override
                        public boolean act(float delta) {
                            loadingLabel.setText("Incorrect password, please try again.");
                            return true;
                        }
                    }));
                }
            }
        });
        
        table.row();
        textButton = new TextButton("Close", skin);
        table.add(textButton).center();
        
        loginWindow.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.ENTER) {
                    final String name = (String) buttonGroup.getChecked().getUserObject();
                    if (checkPassword(name, textField.getText())) {
                        loadingLabel.setText("Signing in...");
                        loadingLabel.addAction(Actions.delay(2.0f, new Action() {
                            @Override
                            public boolean act(float delta) {
                                loadingLabel.setText("Logged in successfully.");
                                if (accountWindow != null) {
                                    accountWindow.remove();
                                }
                                showAccount(name);
                                return true;
                            }
                        }));
                    } else {
                        loadingLabel.setText("Signing in...");
                        loadingLabel.addAction(Actions.delay(2.0f, new Action() {
                            @Override
                            public boolean act(float delta) {
                                loadingLabel.setText("Incorrect password, please try again.");
                                return true;
                            }
                        }));
                    }
                }
                return true;
            }
            
        });
        
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                loginWindow.remove();
                loginWindow = null;
            }
        });
    }
    
    private boolean checkPassword(String name, String password) {
        return nameToPasswords.get(name).equals(password);
    }
    
    private void showBruteForce() {
        bruteForceWindow = new Window("Brute Force Hack", skin);
        bruteForceWindow.getTitleLabel().setStyle(skin.get("window-title", LabelStyle.class));
        bruteForceWindow.getTitleTable().getCells().first().fill(false);
        stage.addActor(bruteForceWindow);
        bruteForceWindow.setSize(400.0f, 400.0f);
        bruteForceWindow.setPosition(MathUtils.random(0, Gdx.graphics.getWidth() - (int) bruteForceWindow.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight() - (int) bruteForceWindow.getHeight()), Align.bottomLeft);
        
        final Table table = new Table();
        final ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setFadeScrollBars(false);
        bruteForceWindow.add(scrollPane).grow().pad(20.0f);
        stage.setScrollFocus(table);
        
        Label label = new Label("[GREEN]Type [RED]./bf.sh <ip address>[] to begin hack.\n\nEx. [RED]./bf.sh 192.168.1.1[]", skin, "markup");
        label.setWrap(true);
        table.add(label).growX();
        
        table.defaults().padTop(15.0f).colspan(1).space(5.0f);
        table.row();
        final Label progressLabel = new Label("", skin);
        progressLabel.setWrap(true);
        progressLabel.setAlignment(Align.topLeft);
        table.add(progressLabel).grow();
        
        bruteForceWindow.row();
        final TextField textField = new TextField("", skin, "prompt");
        bruteForceWindow.add(textField).growX().pad(20.0f);
        stage.setKeyboardFocus(textField);
        
        bruteForceWindow.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                    int pointer, int button) {
                stage.setScrollFocus(table);
                stage.setKeyboardFocus(textField);
                return super.touchDown(event, x, y, pointer, button);
            }
            
        });
        
        bruteForceWindow.row();
        TextButton textButton = new TextButton("Close", skin);
        bruteForceWindow.add(textButton);
        
        bruteForceWindow.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.ENTER) {
                    if (textField.getText().startsWith("./bf.sh ")) {
                        FileHandle file = Gdx.files.local("hacker_simulator_data/text/dictionary.txt");
                        Array<String> strings = new Array<String>(file.readString().split("\\r?\\n"));
                        progressLabel.setText("");
                        String name = ipToNames.get(textField.getText().substring(8), null);
                        if (name != null) {
                            float accumulator = 0;
                            for (final String string : strings) {
                                accumulator += .05f;
                                progressLabel.addAction(Actions.delay(accumulator, new Action() {
                                    @Override
                                    public boolean act(float delta) {
                                        progressLabel.setText(progressLabel.getText() + "\n" + string);
                                        scrollPane.layout();
                                        scrollPane.scrollTo(0, 0, 0, 0);
                                        return true;
                                    }
                                }));
                            }
                            accumulator += .05f;
                            
                            final String password = nameToPasswords.get(name, null);
                            boolean canBeBF = canBeBFlist.get(name, false);
                            if (canBeBF) {
                                progressLabel.addAction(Actions.delay(accumulator, new Action() {
                                    @Override
                                    public boolean act(float delta) {
                                        progressLabel.setText(progressLabel.getText() + "\n\n" + "Password successfully hacked: \n" + password);
                                        scrollPane.layout();
                                        scrollPane.scrollTo(0, 0, 0, 0);
                                        return true;
                                    }
                                }));
                            } else {
                                progressLabel.addAction(Actions.delay(accumulator, new Action() {
                                    @Override
                                    public boolean act(float delta) {
                                        progressLabel.setText(progressLabel.getText() + "\n\n" + "Password hack failed!");
                                        scrollPane.layout();
                                        scrollPane.scrollTo(0, 0, 0, 0);
                                        return true;
                                    }
                                }));
                            }
                        } else {
                            progressLabel.setText(progressLabel.getText() + "\n\n" + "Address incorrect or does not exist.");
                            scrollPane.layout();
                            scrollPane.scrollTo(0, 0, 0, 0);
                        }
                    } else {
                        progressLabel.setText("Unknown command, please try again.");
                    }
                }
                return true;
            }
        });
        
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                bruteForceWindow.remove();
                bruteForceWindow = null;
            }
        });
    }
    
    private void showVulnerability() {
        vulnerabilityWindow = new Window("Vulnerability Hack", skin);
        vulnerabilityWindow.getTitleLabel().setStyle(skin.get("window-title", LabelStyle.class));
        vulnerabilityWindow.getTitleTable().getCells().first().fill(false);
        stage.addActor(vulnerabilityWindow);
        vulnerabilityWindow.setSize(400.0f, 400.0f);
        vulnerabilityWindow.setPosition(MathUtils.random(0, Gdx.graphics.getWidth() - (int) vulnerabilityWindow.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight() - (int) vulnerabilityWindow.getHeight()), Align.bottomLeft);
        
        final Table table = new Table();
        final ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setFadeScrollBars(false);
        vulnerabilityWindow.add(scrollPane).grow().pad(20.0f);
        stage.setScrollFocus(table);
        
        Label label = new Label("[GREEN]Type [RED]./exploit.sh <ip address>[] to begin hack.\n\nEx. [RED]./exploit.sh 192.168.1.1[]", skin, "markup");
        label.setWrap(true);
        table.add(label).growX();
        
        table.defaults().padTop(15.0f).colspan(1).space(5.0f);
        table.row();
        final Label progressLabel = new Label("", skin);
        progressLabel.setWrap(true);
        progressLabel.setAlignment(Align.topLeft);
        table.add(progressLabel).grow();
        
        vulnerabilityWindow.row();
        final TextField textField = new TextField("", skin, "prompt");
        vulnerabilityWindow.add(textField).growX().pad(20.0f);
        stage.setKeyboardFocus(textField);
        
        vulnerabilityWindow.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                    int pointer, int button) {
                stage.setScrollFocus(table);
                stage.setKeyboardFocus(textField);
                return super.touchDown(event, x, y, pointer, button);
            }
            
        });
        
        vulnerabilityWindow.row();
        TextButton textButton = new TextButton("Close", skin);
        vulnerabilityWindow.add(textButton);
        
        vulnerabilityWindow.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.ENTER) {
                    if (textField.getText().startsWith("./exploit.sh ")) {
                        FileHandle file = Gdx.files.local("hacker_simulator_data/text/vulnerabilities.txt");
                        Array<String> strings = new Array<String>(file.readString().split("\\r?\\n"));
                        progressLabel.setText("");
                        String name = ipToNames.get(textField.getText().substring(13), null);
                        if (name != null) {
                            float accumulator = 0;
                            for (final String string : strings) {
                                accumulator += .05f;
                                progressLabel.addAction(Actions.delay(accumulator, new Action() {
                                    @Override
                                    public boolean act(float delta) {
                                        progressLabel.setText(progressLabel.getText() + "\n" + string);
                                        scrollPane.layout();
                                        scrollPane.scrollTo(0, 0, 0, 0);
                                        return true;
                                    }
                                }));
                            }
                            accumulator += .05f;
                            
                            final String password = nameToPasswords.get(name, null);
                            boolean canBeVB = canBeVBlist.get(name, false);
                            if (canBeVB) {
                                progressLabel.addAction(Actions.delay(accumulator, new Action() {
                                    @Override
                                    public boolean act(float delta) {
                                        progressLabel.setText(progressLabel.getText() + "\n\n" + "Vulnerability found on target. Password:\n" + password);
                                        scrollPane.layout();
                                        scrollPane.scrollTo(0, 0, 0, 0);
                                        return true;
                                    }
                                }));
                            } else {
                                progressLabel.addAction(Actions.delay(accumulator, new Action() {
                                    @Override
                                    public boolean act(float delta) {
                                        progressLabel.setText(progressLabel.getText() + "\n\n" + "No vulnerabilities found.");
                                        scrollPane.layout();
                                        scrollPane.scrollTo(0, 0, 0, 0);
                                        return true;
                                    }
                                }));
                            }
                        } else {
                            progressLabel.setText(progressLabel.getText() + "\n\n" + "Address incorrect or does not exist.");
                            scrollPane.layout();
                            scrollPane.scrollTo(0, 0, 0, 0);
                        }
                    } else {
                        progressLabel.setText("Unknown command, please try again.");
                    }
                }
                return true;
            }
            
        });
        
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                vulnerabilityWindow.remove();
                vulnerabilityWindow = null;
            }
        });
    }
    
    private void showSendMail() {
        sendMailWindow = new Window("Send a new E-Mail", skin);
        sendMailWindow.getTitleLabel().setStyle(skin.get("window-title", LabelStyle.class));
        sendMailWindow.getTitleTable().getCells().first().fill(false);
        stage.addActor(sendMailWindow);
        sendMailWindow.setSize(400.0f, 400.0f);
        sendMailWindow.setPosition(MathUtils.random(0, Gdx.graphics.getWidth() - (int) sendMailWindow.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight() - (int) sendMailWindow.getHeight()), Align.bottomLeft);
        
        Table table = new Table();
        sendMailWindow.add(table).pad(5.0f).grow();
        
        table.defaults().space(10.0f);
        Label label = new Label("From:", skin);
        table.add(label).right();
        
        final TextField fromField = new TextField("admin@acme.com", skin);
        table.add(fromField).growX();
        
        table.row();
        label = new Label("To:", skin);
        table.add(label).right();
        
        final TextField toField = new TextField("", skin);
        table.add(toField).growX();
        stage.setKeyboardFocus(toField);
        
        table.row();
        label = new Label("Subject:", skin);
        table.add(label).right();
        
        final TextField subjectField = new TextField("Your password has expired.", skin);
        table.add(subjectField).growX();
        
        table.row();
        label = new Label("Message:", skin);
        table.add(label);
        
        table.row();
        final TextArea textArea = new TextArea("Your password has expired. Please click here to update your password: http://acme.mypassword.com/login\n\nThis is an official email from ACME account management.\n\nCopyright ACME 2017", skin);
        table.add(textArea).colspan(2).grow();
        
        table.row();
        Table buttonTable = new Table();
        table.add(buttonTable).colspan(2).growX();
        
        buttonTable.defaults().space(5.0f);
        TextButton textButton = new TextButton("Send", skin);
        buttonTable.add(textButton).expandX().right();
        
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                String name = toField.getText();
                if (name.endsWith("@acme.com")) {
                    name = name.replace("@acme.com", "");
                    if (canBePhishedList.get(name, false)) {
                        stage.addAction(Actions.delay(10.0f, new Action() {
                            @Override
                            public boolean act(float delta) {
                                createNewMail("Caught a Phish", "botnet@mypassword.com", "hacker@whitehathacker.com", "We were able to capture steph's password: Bananas1", "hacker");
                                return true;
                            }
                        }));
                    }
                }
                
                sendMailWindow.remove();
                sendMailWindow = null;
            }
        });
        
        textButton = new TextButton("Cancel", skin);
        buttonTable.add(textButton);
        
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                sendMailWindow.remove();
                sendMailWindow = null;
            }
        });
    }
    
    private void createNewMail(String subject, String from, String to, String message, String name) {
        GeneratedMail mail = new GeneratedMail();
        mail.to = to;
        mail.from = from;
        mail.subject = subject;
        mail.message = message;
        mail.name = name;
        generatedMails.add(mail);
        Sound sound = Gdx.audio.newSound(Gdx.files.local("hacker_simulator_data/sfx/new email.wav"));
        sound.play();
        
        popNotification();
    }
    
    private class GeneratedMail {
        String to;
        String from;
        String subject;
        String message;
        String name;
    }
    
    private void showReadMail(String name) {
        FileHandle directory = Gdx.files.local("hacker_simulator_data/text/" + name);
        Array<FileHandle> files = new Array<FileHandle>(directory.list());
        
        readMailWindow = new Window(name + "'s Inbox", skin);
        readMailWindow.getTitleLabel().setStyle(skin.get("window-title", LabelStyle.class));
        readMailWindow.getTitleTable().getCells().first().fill(false);
        stage.addActor(readMailWindow);
        readMailWindow.setSize(600.0f, 400.0f);
        readMailWindow.setPosition(MathUtils.random(0, Gdx.graphics.getWidth() - (int) readMailWindow.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight() - (int) readMailWindow.getHeight()), Align.bottomLeft);
        
        Table table = new Table();
        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFlickScroll(false);
        readMailWindow.add(scrollPane).grow().pad(20.0f);
        stage.setScrollFocus(scrollPane);
        
        Label label = new Label("From", skin);
        table.add(label);
        
        label = new Label("Subject", skin);
        table.add(label).expandX();
        
        for (final FileHandle file : files) {
            Array<String> strings = new Array<String>(file.readString().split("\\r?\\n"));
            String from = strings.first();
            String subject = file.name();
            table.row();
            TextButton textButton = new TextButton(from, skin, "email");
            table.add(textButton).fillX();
            
            textButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event,
                        Actor actor) {
                    if (readIndividualMailWindow == null) {
                        showReadIndividualMail(file);
                    } else {
                        readIndividualMailWindow.remove();
                        showReadIndividualMail(file);
                    }
                }
            });
            
            textButton = new TextButton(subject, skin, "email");
            table.add(textButton).fillX();
            
            textButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event,
                        Actor actor) {
                    if (readIndividualMailWindow == null) {
                        showReadIndividualMail(file);
                    } else {
                        readIndividualMailWindow.remove();
                        showReadIndividualMail(file);
                    }
                }
            });
        }
        
        for (final GeneratedMail email : generatedMails) {
            if (email.name.equals(name)) {
                table.row();
                TextButton textButton = new TextButton(email.from, skin, "email");
                table.add(textButton).fillX();

                textButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent event,
                            Actor actor) {
                        if (readIndividualMailWindow == null) {
                            showReadIndividualMail(email.subject, email.from, email.to, email.message);
                        } else {
                            readIndividualMailWindow.toFront();
                        }
                    }
                });

                textButton = new TextButton(email.subject, skin, "email");
                table.add(textButton).fillX();

                textButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent event,
                            Actor actor) {
                        if (readIndividualMailWindow == null) {
                            showReadIndividualMail(email.subject, email.from, email.to, email.message);
                        } else {
                            readIndividualMailWindow.toFront();
                        }
                    }
                });
            }
        }
        
        table.row();
        table.add().expandY();
        
        readMailWindow.row();
        TextButton textButton = new TextButton("Close", skin);
        readMailWindow.add(textButton).center();
        
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                readMailWindow.remove();
                readMailWindow = null;
            }
        });
    }
    
    private void showReadIndividualMail(String subject, String from, String to, String message) {
        readIndividualMailWindow = new Window("email", skin);
        readIndividualMailWindow.getTitleLabel().setStyle(skin.get("window-title", LabelStyle.class));
        readIndividualMailWindow.getTitleTable().getCells().first().fill(false);
        stage.addActor(readIndividualMailWindow);
        readIndividualMailWindow.setSize(600.0f, 400.0f);
        readIndividualMailWindow.setPosition(MathUtils.random(0, Gdx.graphics.getWidth() - (int) readIndividualMailWindow.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight() - (int) readIndividualMailWindow.getHeight()), Align.bottomLeft);
        
        Table table = new Table();
        readIndividualMailWindow.add(table).pad(5.0f).grow();
        
        table.defaults().space(10.0f);
        Label label = new Label("From:", skin);
        table.add(label).right();
        
        label = new Label(from, skin, "box");
        table.add(label).growX();
        
        table.row();
        label = new Label("To:", skin);
        table.add(label).right();
        
        label = new Label(to, skin, "box");
        label.setWrap(true);
        table.add(label).growX();
        
        table.row();
        label = new Label("Subject:", skin);
        table.add(label).right();
        
        label = new Label(subject, skin, "box");
        table.add(label).growX();
        
        table.row();
        label = new Label("Message:", skin);
        table.add(label);
        
        table.row();
        TextArea textArea = new TextArea(message, skin);
        textArea.setDisabled(true);
        table.add(textArea).colspan(2).grow();
        
        table.row();
        
        TextButton textButton = new TextButton("Close", skin);
        table.add(textButton).colspan(2);
        
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                readIndividualMailWindow.remove();
                readIndividualMailWindow = null;
            }
        });
    }
    
    private void showReadIndividualMail(FileHandle file) {
        String subject = file.name();
        Array<String> strings = new Array<String>(file.readString().split("\\r?\\n"));
        String from = strings.first();
        String to = strings.get(1);
        String message = strings.get(2);
        
        showReadIndividualMail(subject, from, to, message);
    }
    
    private void showAccount(final String name) {
        accountWindow = new Window("Logged in as " + name, skin);
        accountWindow.getTitleLabel().setStyle(skin.get("window-title", LabelStyle.class));
        accountWindow.getTitleTable().getCells().first().fill(false);
        stage.addActor(accountWindow);
        accountWindow.setSize(400.0f, 400.0f);
        accountWindow.setPosition(MathUtils.random(0, Gdx.graphics.getWidth() - (int) accountWindow.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight() - (int) accountWindow.getHeight()), Align.bottomLeft);
        
        accountWindow.defaults().space(10.0f);
        Label label = new Label("Welcome " + name + "!", skin);
        accountWindow.add(label).colspan(3);
        
        accountWindow.row();
        DesktopButton mailButton = new DesktopButton("mail", skin);
        accountWindow.add(mailButton).width(100.0f);
        
        accountWindow.row();
        DesktopButton virusButton = new DesktopButton("virus", skin);
        accountWindow.add(virusButton).width(100.0f);
        
        DesktopButton ransomButton = new DesktopButton("ransom", skin);
        accountWindow.add(ransomButton).width(100.0f);
        
        DesktopButton deleteButton = new DesktopButton("delete", skin);
        accountWindow.add(deleteButton).width(100.0f);
        
        accountWindow.row();
        final Label statusLabel = new Label("", skin);
        accountWindow.add(statusLabel).colspan(3);
        
        mailButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                statusLabel.setText("Email Inbox opened successfully.");
                if (readMailWindow != null) {
                    readMailWindow.remove();
                }
                showReadMail(name);
            }
        });
        
        virusButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                statusLabel.setText("Installed \"keylogger.exe\" successfully.\nYou own all secrets typed on this terminal.");
                if (name.equals("larry")) {
                    stage.addAction(Actions.delay(30.0f, new Action() {
                        @Override
                        public boolean act(float delta) {
                            createNewMail("New User Activity", "larry@acme.com", "hacker@whitehathacker.com", "New user activity @ terminal 192.168.1.103\nbing.com\nhow to fire your employee\nhow to make your employee quit\nroot\n%fve5zeT@\nyahoo.com\nstock market", "hacker");
                            return true;
                        }
                    }));
                } else {
                    
                    stage.addAction(Actions.delay(30.0f, new Action() {
                        @Override
                        public boolean act(float delta) {
                            String output = "google.com";
                            
                            Array<String> strings = new Array<String>(Gdx.files.local("hacker_simulator_data/text/key logger.txt").readString().split("\\r?\\n"));
                            for (int i = 0; i < MathUtils.random(3,6); i++) {
                                output += "\n" + strings.random();
                            }
                            
                            createNewMail("New User Activity", name + "@acme.com", "hacker@whitehathacker.com", output, "hacker");
                            return true;
                        }
                    }));
                }
            }
        });
        
        ransomButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                statusLabel.setText("Installed \"ransomware.exe\" successfully.\nUser must pay a fee to decrypt their data.");
            }
        });
        
        deleteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                statusLabel.setText("All personal files and photos have been deleted.");
            }
        });
        
        accountWindow.row();
        TextButton textButton = new TextButton("Close", skin);
        accountWindow.add(textButton).expand().colspan(3).bottom().padBottom(10.0f);
        
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                accountWindow.remove();
                accountWindow = null;
            }
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act();
        stage.draw();
        
        if (Gdx.input.isKeyJustPressed(Keys.F5)) {
            dispose();
            create();
            
            loginWindow = null;
            bruteForceWindow = null;
            vulnerabilityWindow = null;
            sendMailWindow = null;
            readIndividualMailWindow = null;
            readMailWindow = null;
            accountWindow = null;
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
    }
}
