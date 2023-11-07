package lotto.controller;

import java.util.List;
import lotto.domain.BonusNumber;
import lotto.domain.Lotto;
import lotto.domain.LottoData;
import lotto.domain.User;
import lotto.service.LottoService;
import lotto.utils.InputProcessor;
import lotto.utils.Validation;
import lotto.view.ExceptionMessages;
import lotto.view.InputView;
import lotto.view.OutputView;

public class LottoController {

    private User user;
    private LottoData lottoData;
    private final LottoService lottoService;
    private final OutputView outputView;
    private final InputProcessor inputProcessor;

    public LottoController(LottoService lottoService, InputView inputView, OutputView outputView) {
        this.lottoService = lottoService;
        this.outputView = outputView;
        this.inputProcessor = new InputProcessor(inputView);
    }

    public void run() {
        beforeStart();
        setLottoData();
    }

    private void beforeStart() {
        beforeStartRecursive();
    }

    private void beforeStartRecursive() {
        try {
            outputView.getInputAmount();
            int purchaseAmount = inputProcessor.getUserInputPurchaseAmount();
            user = new User(purchaseAmount);
            lottoService.buyLottoAll(user);
            printBuyLotto();
        } catch (NumberFormatException e) {
            System.out.println(ExceptionMessages.STRING_TO_INTEGER.getMessage());
            beforeStartRecursive();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            beforeStartRecursive();
        }
    }

    private void printBuyLotto() {
        outputView.printLottoCount(user.getPurchaseAmount() / 1_000);
        for (Lotto lotto : user.getLottos()) {
            outputView.printLottoNumbers(lotto.getNumbers());
        }
    }

    private void setLottoData() {
        setWinningNumbers();
        setBonusNumber();
        lottoData = lottoService.setWinningNumbers(lottoData.winningNumbers(), lottoData.bonusNumber());
    }

    private Lotto setWinningNumbers() {
        try {
            outputView.getInputWinningNumbers();
            List<Integer> userInputWinningNumbers = inputProcessor.getUserInputWinningNumbers();
            lottoData = new LottoData(new Lotto(userInputWinningNumbers),null);
            return lottoData.winningNumbers();
        } catch (NumberFormatException e) {
            System.out.println(ExceptionMessages.STRING_TO_INTEGER.getMessage());
            return setWinningNumbers();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return setWinningNumbers();
        }
    }

    private BonusNumber setBonusNumber() {
        try {
            outputView.getInputBonusNumber();
            int bonusNumber = inputProcessor.getUserInputBonusNumber();
            Validation.validateBonusNumberNotInWinningNumber(bonusNumber, lottoData.winningNumbers().getNumbers());
            return new BonusNumber(bonusNumber);
        } catch (NumberFormatException e) {
            System.out.println(ExceptionMessages.STRING_TO_INTEGER.getMessage());
            return setBonusNumber();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return setBonusNumber();
        }
    }
}
