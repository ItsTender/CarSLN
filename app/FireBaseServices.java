public class FireBaseServices {
        private static FirebaseServices instance;
        private FirebaseAuth auth;

        private FirebaseFirestore store;
        private FirebaseStorage storage;

        public FirebaseAuth getAuth() {
            return auth;
        }

        public FirebaseFirestore getStore() {
            return store;
        }

        public FirebaseStorage getStorage() {
            return storage;
        }

        private FirebaseServices()
        {
            this.auth= FirebaseAuth.getInstance();
            this.store= FirebaseFirestore.getInstance();
            this.storage= FirebaseStorage.getInstance();
        }
        public static FirebaseServices getInstance()
        {
            if(instance==null) instance = new FirebaseServices();

            return instance;
        }
}
