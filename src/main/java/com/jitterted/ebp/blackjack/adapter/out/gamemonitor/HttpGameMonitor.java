package com.jitterted.ebp.blackjack.adapter.out.gamemonitor;

import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.GameMonitor;
import org.springframework.web.client.RestTemplate;

public class HttpGameMonitor implements GameMonitor {
  @Override
  public void roundCompleted(Game game) {
    GameResultDto dto = GameResultDto.of(game);
    try {
      post("https://blackjack-game-monitor.herokuapp.com/api/gameresults",
           dto);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void post(String uri, GameResultDto gameResultDto) throws Exception {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.postForObject(uri, gameResultDto, GameResultDto.class);
  }

}
