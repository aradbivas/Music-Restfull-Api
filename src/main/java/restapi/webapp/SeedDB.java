//package restapi.webapp;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.lang.reflect.Array;
//import java.util.Arrays;
//
//@Configuration
//public class SeedDB {
//    private static final Logger logger = LoggerFactory.getLogger(SeedDB.class);
//
//    /*
//    CommandLineRunner is an interface that indicates the beans are contained
//    within a SpringApplication
//     */
//
//    @Bean
//    CommandLineRunner seedDatabase(UserRepo profileRepos, PlaylistRepo postRepo, SongRepo songRepo){
//        return args -> {
//            User profile = profileRepos.save(new User("arad","arad@Gmail.com"));
//            Song song1 = new Song("song1");
//            songRepo.save(song1);
//            Playlist post2 = postRepo.save(new Playlist("title2", song1, profile));
//            Playlist post1 = postRepo.save(new Playlist("title1", song1,profile));
//            profile.setPlaylists(Arrays.asList(post1,post2));
//            profileRepos.save(profile);
//        };
//    }
//}
