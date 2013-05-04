package com.coded.jgoogle;

/**
 * JGoogle.java
 * permet de faire des recherches google et d'utiliser une partie de
 * l'algorithme de Google fourni avec le GoogleApi
 * utiliser "spell" pour vérifier l'orthographe d'un mot
 * Date: 30/12/2005
 * @author Hassen Ben Tanfous
 */

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import com.google.soap.search.*;

public class JGoogle extends JFrame implements ActionListener {

    /** de préférence enregistrez vous et utilisez votre propre clé pour ne pas être
     * limité par Google dans le nombre de recherches
     */
    private static final String googleKey = "";


    private JTextField
            txtQueury,
            txtLang,
            txtStartRes,
            txtMaxRes,
            txtCountRes;

    private JRadioButton
            radioFiltre,
            radioRestricts,
            radioSafe;

    private JLabel
            lblQueury,
            lblLang,
            lblMaxRes,
            lblStartRes,
            lblCountRes;

    private JScrollPane scroll;
    private JEditorPane editor;

    private JCheckBox
            googleTitle,
            googleSummary,
            googleSnippet,
            googleHostName,
            googleDirectoryTitle,
            googleCachedSize,
            googleUrl;

    private Container cont;

    private GoogleSearch gs;
    private GoogleSearchResult gsr;
    private GoogleSearchResultElement[] gsre;

    public JGoogle() {
        instancierComposants();
        configurerComposants();
    }

    private void configurerComposants() {
        gs.setKey(googleKey);

        cont.setLayout(null);

        //modèle JTextField
        txtQueury.setToolTipText("recherche et spell pour vérification");
        txtLang.setToolTipText("fr, en, de");
        txtStartRes.setToolTipText("Set the index of the first result to be " +
                                   "returned. For instance if there are 137 results, you may want " +
                                   "to start at 20");
        txtMaxRes.setToolTipText(
                "Set the maximum number of results to be returned");
        txtMaxRes.setText("10");
        txtCountRes.setToolTipText(
                "Returns the estimated total number of results returned for the query");
        txtCountRes.setEditable(false);

        //modèle RadioButton
        radioFiltre.setToolTipText(
                "This filter eliminates results that are very similar");
        radioRestricts.setToolTipText(
                "This allows you to restrict the search to a specific document");
        radioSafe.setToolTipText("When SafeSearch is turned on, sites and web pages containing pornography and explicit sexual content are blocked from search results");

        //modèle editor
        editor.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        editor.setPage(e.getURL());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        editor.setEditable(false);
        editor.setContentType("text/html");

        //modèle JScrollPane
        scroll.setViewportView(editor);

        //modèle JCheckBox
        googleTitle.setToolTipText("Titre du résultat de la recherche");
        googleSummary.setToolTipText("Retourne un résumé du document");
        googleSnippet.setToolTipText("Retourne un fragment du document");
        googleHostName.setToolTipText("Affiche le nom du Host");
        googleDirectoryTitle.setToolTipText("Retourne le nom du dossier");
        googleCachedSize.setToolTipText("Retourne la taille du cache");

        googleTitle.setSelected(true);
        googleSummary.setSelected(true);
        googleSnippet.setSelected(true);

        //écouteurs JTextField
        txtQueury.addActionListener(this);

        //ajout des composants au Container
        ajouterComposant(cont, txtQueury, 20, 46, 365, 27);
        ajouterComposant(cont, radioFiltre, 809, 46, 100, 24);
        ajouterComposant(cont, radioRestricts, 809, 83, 100, 24);
        ajouterComposant(cont, radioSafe, 809, 124, 100, 24);

        ajouterComposant(cont, scroll, 20, 91, 750, 600);

        ajouterComposant(cont, txtLang, 809, 160, 20, 24);
        ajouterComposant(cont, lblLang, 829, 160, 100, 24);
        ajouterComposant(cont, txtMaxRes, 809, 194, 50, 24);
        ajouterComposant(cont, lblMaxRes, 859, 194, 100, 24);

        ajouterComposant(cont, txtStartRes, 809, 258, 50, 24);
        ajouterComposant(cont, lblStartRes, 859, 258, 100, 24);
        ajouterComposant(cont, txtCountRes, 809, 292, 150, 24);

        ajouterComposant(cont, googleTitle, 806, 340, 100, 24);
        ajouterComposant(cont, googleSummary, 806, 360, 100, 24);
        ajouterComposant(cont, googleSnippet, 806, 380, 100, 24);
        ajouterComposant(cont, googleHostName, 806, 400, 100, 24);
        ajouterComposant(cont, googleDirectoryTitle, 806, 420, 100, 24);
        ajouterComposant(cont, googleCachedSize, 806, 440, 100, 24);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setTitle("JGoogle par Hassen Ben Tanfous");
        setLocation(new Point(0, 0));
        setSize(new Dimension(1024, 768));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void ajouterComposant(Container c, Component comp, int x, int y,
                                  int x1, int y1) {
        comp.setBounds(x, y, x1, y1);
        c.add(comp);
    }


    /**
     * instancierComposants : initialise les composants
     * void
     */
    private void instancierComposants() {
        //Container
        cont = getContentPane();

        //JTextField
        txtQueury = new JTextField("");
        txtLang = new JTextField("en");
        txtStartRes = new JTextField("0");
        txtMaxRes = new JTextField("");
        txtCountRes = new JTextField("");

        //RadioButton
        radioFiltre = new JRadioButton("Filtre");
        radioRestricts = new JRadioButton("Restricts");
        radioSafe = new JRadioButton("SafeSearch");

        //Label
        lblQueury = new JLabel("Recherche");
        lblLang = new JLabel("Langues");
        lblMaxRes = new JLabel("Résultats Max");
        lblStartRes = new JLabel("Start index");
        lblCountRes = new JLabel("Total");

        //JEditorPane
        editor = new JEditorPane();

        //JScrollPane
        scroll = new JScrollPane(editor);

        //JCheckBox
        googleTitle = new JCheckBox("Titre");
        googleSummary = new JCheckBox("Sommaire");
        googleSnippet = new JCheckBox("Fragment");
        googleHostName = new JCheckBox("Host");
        googleDirectoryTitle = new JCheckBox("Titre Dossier");
        googleCachedSize = new JCheckBox("Taille cache");
        googleUrl = new JCheckBox("URL");

        //Google
        gs = new GoogleSearch();
        gsr = new GoogleSearchResult();
        gsre = null;
    }

    /**
     * actionPerformed: analyse les actions et les évènements du user
     */
    public void actionPerformed(ActionEvent e) {
        int maxRes,
                startRes;
        if (e.getSource() == txtQueury) {
            if (txtQueury.getText().equals("spell")) {
                new SpellingSearch();
            }

            //vérification du nombre de résultats maximum
            maxRes = Integer.parseInt(txtMaxRes.getText());
            if (maxRes >= 0 && maxRes <= 10) {
                gs.setMaxResults(maxRes);
            } else {
                msg("maxResultats >=0 && <=10", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }

            //ajouter la langue
            gs.setLanguageRestricts("lang_" + txtLang.getText());

            //index résultat
            startRes = Integer.parseInt(txtStartRes.getText());
            if (startRes >= 0) {
                gs.setStartResult(startRes);
            } else {
                msg("start index Res >= 0", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

            //ajouter la configuration avancée
            if (radioFiltre.isSelected()) {
                gs.setFilter(true);
            }
            if (radioRestricts.isSelected()) {
                gs.setRestrict(txtQueury.getText());
            }
            if (radioSafe.isSelected()) {
                gs.setSafeSearch(true);
            }

            //faire la recherche
            new Recherche().start();
        }
    }


    private void msg(String msg, String titre, int type) {
        JOptionPane.showMessageDialog(null, msg, titre, type);
    }

    /**
     * permet de faire la recherche google
     */
    private class Recherche extends Thread {
        public void run() {
            search();
        }

        /** search : permet de faire des recherches Google
         * void
         */
        private void search() {
            String result = "<body bgcolor=\"black\"> " +
                            "<font color=\"#009933\"";
            gs.setQueryString(txtQueury.getText());
            try {
                gsr = gs.doSearch();
                txtCountRes.setText("" + gsr.getEstimatedTotalResultsCount());
                gsre = gsr.getResultElements();

                for (int i = 0; i < gsre.length; i++) {
                    if (googleTitle.isSelected()) {
                        result +=
                                "<font color=\"red\"><u><b>Title: </u></b><a href=\"" +
                                gsre[i].getURL() + "\">"
                                + gsre[i].getTitle() + "</a><br>" +
                                "<br><b>URL: " +
                                gsre[i].getHostName() + gsre[i].getURL() +
                                "</b></font><br>";
                    }
                    if (googleSummary.isSelected()) {
                        result += "<u><b>Summary: </u></b>" +
                                gsre[i].getSummary() +
                                "<br>";
                    }
                    if (googleSnippet.isSelected()) {
                        result += "<u><b>Snippet: </u></b>" +
                                gsre[i].getSnippet() +
                                "<br>";
                    }
                    if (googleHostName.isSelected()) {
                        result += "<u><b>Host: </u></b>" + gsre[i].getHostName() +
                                "<br>";
                    }
                    if (googleDirectoryTitle.isSelected()) {
                        result += "<u><b>Directory Title: </u></b>" +
                                gsre[i].getDirectoryTitle() + "<br>";
                    }
                    if (googleCachedSize.isSelected()) {
                        result += "<u><>Cached Size: </u></b>" +
                                gsre[i].getCachedSize() + "<br>";
                    }
                    result += "<br>";
                }
            } catch (GoogleSearchFault e) {
                msg("Erreur d'exécution " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            }

            //affichage de la recherche
            editor.setText(result);
        }
    } //fin de la  classe Recherche


    /**
     * permet de faire une vérification de l'orthographe
     */
    private class SpellingSearch extends Thread {
        public void run() {
            spelling();
        }

        /**
         * spelling : Vérifie à l'aide de Google pour l'orthographe d'une recherche
         * void:
         */
        private void spelling() {
            String verif = JOptionPane.showInputDialog(
                    "Entrez la recherche à vérifier");
            if (verif.equals(null)) {
                msg("Vous devez entrez une phrase\nRecommencer",
                    "Error Spelling", JOptionPane.ERROR_MESSAGE);
            }
            try {
                String spell = gs.doSpellingSuggestion(verif);
                msg(spell, "Spelling Solution", JOptionPane.INFORMATION_MESSAGE);
            } catch (GoogleSearchFault e) {
                e.printStackTrace();
            }
        }
    } //fin de la classe SpellingSearch
}
