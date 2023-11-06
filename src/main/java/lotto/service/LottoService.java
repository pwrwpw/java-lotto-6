package lotto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lotto.domain.Lotto;
import lotto.domain.User;
import lotto.utils.NumberGenerator;
import lotto.utils.RandomNumberGenerator;

public class LottoService {
    private final NumberGenerator numberGenerator;
    public LottoService(NumberGenerator numberGenerator) {
        this.numberGenerator = new RandomNumberGenerator();
    }
    public void buyLottoAll(User user) {
        int purchaseAmount = user.getPurchaseAmount();
        int lottoCount = purchaseAmount / 1000;

        IntStream.range(0, lottoCount)
                .forEach(i -> buyLottoOne(user));
    }

    private void buyLottoOne(User user){
        Lotto lotto = generateLottoNumber();
        user.addLotto(lotto);
    }
    private Lotto generateLottoNumber() {
        List<Integer> lottoNumbers = new ArrayList<>(
                numberGenerator.generateNumbers(1, 45, 6));
        return new Lotto(lottoNumbers);
    }
}
