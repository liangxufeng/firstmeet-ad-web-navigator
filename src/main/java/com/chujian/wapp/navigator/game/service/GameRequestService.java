package com.chujian.wapp.navigator.game.service;


import com.chujian.wapp.navigator.game.entity.Game;
import com.chujian.wapp.navigator.game.respository.GameRepository;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameRequestService {

  @Autowired
  private GameRepository gameRepository;

  public void putGameToRequest(HttpServletRequest request, String gameId) {
    Game game = gameRepository.findByGameId(gameId);
    request.setAttribute("game", game);
  }

}
