package com.mgiandia.library.soap;

import javax.xml.ws.Endpoint;



public class LibraryServicePublisher {
    public static void main(String[ ] args) {

        //HACK αρχικοποιούμε τη βάση δεδομένων και μάλιστα με κώδικα που ανήκει
        // στον κώδικα ελέγχου. Κανονικά δεν θα πρέπει να υπάρχει κάτι τέτοιο
        
        // Η πρώτη παράμετρος είναι το URL που δημοσιεύεται η υπηρεσία
        // Η δεύτερη παράμετρος είναι το SIB δηλ. η κλάση που υλοποιεί το SEI
        Endpoint.publish("http://localhost:9877/library", new LibraryServiceImpl());
      }

}
