package eu.panic.gamecrashservice.template.service.implement;

import eu.panic.gamecrashservice.template.dto.GameCrashState;
import eu.panic.gamecrashservice.template.dto.UserDto;
import eu.panic.gamecrashservice.template.entity.Game;
import eu.panic.gamecrashservice.template.enums.GameEventType;
import eu.panic.gamecrashservice.template.enums.GameType;
import eu.panic.gamecrashservice.template.exception.InsufficientFundsException;
import eu.panic.gamecrashservice.template.exception.InvalidCredentialsException;
import eu.panic.gamecrashservice.template.hash.GameCrashBetHash;
import eu.panic.gamecrashservice.template.payload.GameCrashPlayRequest;
import eu.panic.gamecrashservice.template.payload.GameCrashSliderEvent;
import eu.panic.gamecrashservice.template.payload.GameCrashTakeResponse;
import eu.panic.gamecrashservice.template.payload.GameCrashTimerEvent;
import eu.panic.gamecrashservice.template.repository.GameCrashBetHashRepository;
import eu.panic.gamecrashservice.template.repository.implement.GameRepositoryImpl;
import eu.panic.gamecrashservice.template.repository.implement.UserRepositoryImpl;
import eu.panic.gamecrashservice.template.service.GameCrashService;
import eu.panic.gamecrashservice.template.util.GameSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class GameCrashServiceImpl implements GameCrashService {
    public GameCrashServiceImpl(GameRepositoryImpl gameRepository, UserRepositoryImpl userRepository, SimpMessagingTemplate simpMessagingTemplate, GameCrashBetHashRepository gameCrashBetHashRepository, RestTemplate restTemplate) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.gameCrashBetHashRepository = gameCrashBetHashRepository;
        this.restTemplate = restTemplate;
    }
    private final GameRepositoryImpl gameRepository;
    private final UserRepositoryImpl userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameCrashBetHashRepository gameCrashBetHashRepository;
    private final RestTemplate restTemplate;
    private static String JWT_URL = "http://localhost:8080/api/auth/getInfoByJwt";
    private static GameCrashState gameCrashState = new GameCrashState();
    @Override
    public void handlePlayCrash(String jwtToken, GameCrashPlayRequest gameCrashPlayRequest) {
        log.info("Starting method handlePlayCrash on service {} method: handlePlayCrash", GameCrashServiceImpl.class);

        log.info("Receiving entity user by jwtToken on service {} method: handlePlayCrash", GameCrashServiceImpl.class);

        ResponseEntity<UserDto> userDtoResponseEntity =
                restTemplate.postForEntity(JWT_URL + "?jwtToken=" + jwtToken, null, UserDto.class);

        if (userDtoResponseEntity.getStatusCode().isError()){
            log.warn("Incorrect JWT token on service {} method: handlePlayCrash", GameCrashServiceImpl.class);
            throw new InvalidCredentialsException("Incorrect JWT token");
        }

        if (gameCrashPlayRequest.getBet() > 100000 || gameCrashPlayRequest.getBet() < 1){
            log.warn("Incorrect Crash data on service {} method: handlePlayCrash", GameCrashServiceImpl.class);
            throw new InvalidCredentialsException("Incorrect Crash data");
        }

        UserDto userDto = userDtoResponseEntity.getBody();

        if (userDto.getBalance() < gameCrashPlayRequest.getBet()){
            log.warn("You do not have enough money for this win on service {} method: handlePlayCrash", GameCrashServiceImpl.class);
            throw new InsufficientFundsException("You do not have enough money for this win");
        }

        if (!userDto.getIsAccountNonLocked()){
            log.warn("You have been temporarily blocked. For all questions contact support on service {}" +
                    "method: handlePlayCrash", GameCrashServiceImpl.class);
            throw new InvalidCredentialsException("You have been temporarily blocked. For all questions contact support");
        }

        if (gameCrashBetHashRepository.findCrashBetHashByUsername(userDto.getUsername()) != null){
            log.warn("You have already placed your win, wait for the game to end on service {} method: handlePlayCrash", GameCrashServiceImpl.class);
            throw new InvalidCredentialsException("You have already placed your win, wait for the game to end");
        }

        if (gameCrashState.getIsStarted()){
            log.warn("You cannot place a win as the game has already started on service {} method: handlePlayCrash", GameCrashServiceImpl.class);
            throw new InvalidCredentialsException("You cannot place a win as the game has already started");
        }

        log.info("Updating user balance by Id on service {} method: handlePlayCrash", GameCrashServiceImpl.class);

        userRepository.updateBalanceById(userDto.getBalance() - gameCrashPlayRequest.getBet(), userDto.getId());

        log.info("Creating new hash crashBetHash on service {} method: handlePlayCrash", GameCrashServiceImpl.class);

        GameCrashBetHash gameCrashBetHash = new GameCrashBetHash();

        gameCrashBetHash.setUsername(userDto.getUsername());
        gameCrashBetHash.setUser(userDto);
        gameCrashBetHash.setBet(gameCrashPlayRequest.getBet());
        gameCrashBetHash.setIsTaken(false);
        gameCrashBetHash.setWin(0L);
        gameCrashBetHash.setCoefficient(1.00);
        gameCrashBetHash.setTimestamp(System.currentTimeMillis());

        log.info("Saving a hash crashBetHash on service {} method: handlePlayCrash", GameCrashServiceImpl.class);

        gameCrashBetHashRepository.save(gameCrashBetHash);

        simpMessagingTemplate.convertAndSend("/bets/topic", gameCrashBetHashRepository.findAll());
    }

    @Override
    public GameCrashTakeResponse handleBetTaking(String jwtToken) {
        log.info("Starting method handleBetTaking on service {} method: handleBetTaking", GameCrashServiceImpl.class);

        double coefficient = gameCrashState.getCoefficient();

        log.info("Receiving entity user by jwtToken on service {} method: handleBetTaking", GameCrashServiceImpl.class);

        ResponseEntity<UserDto> userDtoResponseEntity =
                restTemplate.postForEntity(JWT_URL + "?jwtToken=" + jwtToken, null, UserDto.class);

        if (userDtoResponseEntity.getStatusCode().isError()){
            log.warn("Incorrect JWT token on service {} method: handleBetTaking", GameCrashServiceImpl.class);
            throw new InvalidCredentialsException("Incorrect JWT token");
        }

        UserDto userDto = userDtoResponseEntity.getBody();

        GameCrashBetHash gameCrashBetHash = gameCrashBetHashRepository.findCrashBetHashByUsername(userDto.getUsername());

        if (gameCrashBetHash == null){
            log.warn("You can't withdraw your win since you didn't place it on service {} method: handleBetTaking", GameCrashServiceImpl.class);
            throw new InvalidCredentialsException("You can't withdraw your win since you didn't place it");
        }

        if (!gameCrashState.getIsStarted()){
            log.warn("You can't withdraw your bet as the game hasn't started yet on service {} method: handleBetTaking",
                    GameCrashServiceImpl.class);
            throw new InvalidCredentialsException("You can't withdraw your bet as the game hasn't started yet");
        }

        if (gameCrashBetHash.getIsTaken()){
            log.warn("You've already taken that win on service {} method: handleBetTaking", GameCrashServiceImpl.class);
            throw new InvalidCredentialsException("You've already taken that win");
        }

        long preWin = (long) (gameCrashBetHash.getBet() * coefficient);
        long win = (long)  (preWin - (preWin * 0.03));

        gameCrashBetHash.setWin(win);
        gameCrashBetHash.setIsTaken(true);
        gameCrashBetHash.setCoefficient(coefficient);

        gameCrashBetHashRepository.save(gameCrashBetHash);

        log.info("Updating user balance by Id on service {} method: handlePlayCrash", GameCrashServiceImpl.class);

        userRepository.updateBalanceById(userDto.getBalance() + win, userDto.getId());

        log.info("Creating a response for this method on service {} method: handlePlayCrash", GameCrashServiceImpl.class);

        GameCrashTakeResponse gameCrashTakeResponse = new GameCrashTakeResponse();

        gameCrashTakeResponse.setBet(gameCrashBetHash.getBet());
        gameCrashTakeResponse.setWin(gameCrashBetHash.getWin());
        gameCrashTakeResponse.setCoefficient(gameCrashBetHash.getCoefficient());

        simpMessagingTemplate.convertAndSend("/bets/topic", gameCrashBetHashRepository.findAll());

        return gameCrashTakeResponse;
    }

    @Override
    public List<Game> getLastTwentyCrashGames() {
        log.info("Starting method getLastTwentyCrashGames on service {} method: getLastTwentyCrashGames", GameCrashServiceImpl.class);

        return gameRepository.findLastTwentyGamesByGameType(GameType.CRASH);
    }

    @Override
    public Game getLastCrashGame() {
        log.info("Starting method getLastCrashGame on service {} method: getLastCrashGame", GameCrashServiceImpl.class);

        return gameRepository.findLastGameByGameType(GameType.CRASH);
    }

    @Override
    public List<GameCrashBetHash> getAllCrashBets() {
        log.info("Starting method getAllCrashBets on service {} method: getAllCrashBets", GameCrashServiceImpl.class);

        return (List<GameCrashBetHash>) gameCrashBetHashRepository.findAll();
    }

    @Scheduled(fixedDelay = 5000)
    private void taskPlayCrash() {
        log.info("Starting method taskPlayCrash on service {} method: taskPlayCrash", GameCrashServiceImpl.class);

        gameCrashState.setIsStarted(false);
        gameCrashState.setCoefficient(1.00);

        double currentSeconds = 10d;

        GameCrashTimerEvent gameCrashTimerEvent = new GameCrashTimerEvent();

        gameCrashTimerEvent.setType(GameEventType.TIMER);

        while (currentSeconds >= 0) {
            gameCrashTimerEvent.setValue(Math.floor(currentSeconds * 1e1) / 1e1);

            simpMessagingTemplate.convertAndSend("/slider/topic", gameCrashTimerEvent);

            currentSeconds -= 0.1;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        gameCrashState.setIsStarted(true);

        GameSessionUtil gameSessionUtil = new GameSessionUtil("00000000000000000011b3e92e82e0f9939093dccc3614647686c20e5ebe3aa6", "000000000000000000223b7a2298fb1c6c75fb0efc28a4c56853ff4112ec6bc9");

        gameSessionUtil.generateSalt();

        double crashNumber = Math.floor(Math.max(1, 1000000 / (Math.floor(gameSessionUtil.generateRandomNumber() * 1000000) + 1) * (1 - 0.05)) * 1e1) / 1e1;

        GameCrashSliderEvent gameCrashSliderEvent = new GameCrashSliderEvent();

        gameCrashSliderEvent.setType(GameEventType.SLIDER);

        double start = 1.00;
        int limit  = 10;
        double tempNumber = 0;
        while (true) {
            start+= 0.001;
            if (Math.floor(start * 1e2)/1e2 != tempNumber) {
                gameCrashSliderEvent.setValue(Math.floor(start * 1e2)/1e2);
                //              log.info("{}", Math.floor(start * 1e2)/1e2);
                if (Math.floor(start * 1e2)/1e2 == crashNumber){
                    gameCrashSliderEvent.setCrashed(true);

                    simpMessagingTemplate.convertAndSend("/slider/topic", gameCrashSliderEvent);
                    break;
                }

                gameCrashSliderEvent.setCrashed(false);

                gameCrashState.setCoefficient(Math.floor(start * 1e2)/1e2);

                simpMessagingTemplate.convertAndSend("/slider/topic", gameCrashSliderEvent);
            }
            tempNumber = Math.floor(start * 1e2)/1e2;

            try {
                TimeUnit.MILLISECONDS.sleep((long) (limit));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<GameCrashBetHash> gameCrashBetHashList = (List<GameCrashBetHash>) gameCrashBetHashRepository.findAll();

        long allBets = 0;
        long allWins = 0;

        for (GameCrashBetHash gameCrashBetHash : gameCrashBetHashList){
            allBets += gameCrashBetHash.getBet();
            allWins += gameCrashBetHash.getWin();
        }

        log.info("Deleting all crashBetHashes on service {} method: taskPlayCrash", GameCrashServiceImpl.class);

        gameCrashBetHashRepository.deleteAll();

        Game game = new Game();

        game.setGameType(GameType.CRASH);
        game.setNickname("null");
        game.setUsername("null");
        game.setBet(allBets);
        game.setWin(allWins);
        game.setCoefficient(gameCrashState.getCoefficient());
        game.setClientSeed("000000000000000000223b7a2298fb1c6c75fb0efc28a4c56853ff4112ec6bc9");
        game.setServerSeed("00000000000000000011b3e92e82e0f9939093dccc3614647686c20e5ebe3aa6");
        game.setSalt(gameSessionUtil.getSalt());
        game.setTimestamp(System.currentTimeMillis());

        log.info("Saving an entity game on service {} method: taskPlayCrash", GameCrashServiceImpl.class);

        gameRepository.save(game);

        simpMessagingTemplate.convertAndSend("/bets/topic", new ArrayList<>());
    }
}
