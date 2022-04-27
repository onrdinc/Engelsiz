package com.example.engelsiz;

public class ProfileInfo {

        private String FullName;
        private String phoneNumber;
        private String dateOfBirth;
        private String gender;
        private String city;

        public ProfileInfo() {
            //no argument constructor
        }

        public ProfileInfo(String FullName, String phoneNumber, String dateOfBirth, String gender, String city  ) {


            this.FullName = FullName;
            this.phoneNumber = phoneNumber;
            this.dateOfBirth = dateOfBirth;
            this.gender = gender;
            this.city = city;

        }
        public String getPhoneNumber() {
            return phoneNumber;
        }
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
        public String getDateOfBirth() {
            return dateOfBirth;
        }
        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getFullName() {
            return FullName;
        }


        public void setFullName(String fullName) {
            FullName = fullName;
        }
    }





