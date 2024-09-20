# Fine

Applied when a Lending is overdue.

> [Attributes](#attributes)
>
> [Relevant Use Cases](#Relevant-Use-Cases)
>
> [Customer Clarifications](#Customer-Clarifications)

### Attributes
- [LendingNumber](../ValueObjects/LendingNumber.md)
- Value
- Paid


### Relevant Use Cases
- #### Phase 1
    - (WP#1A.xxx : As Librarian ...

### Customer Clarifications
>[Q: Como é que a fine é aplicada?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28911#p36509)
>
>A: a aplicação da multa é tratada fora do sistema. o sistema deve calcular o valor da multa com base no numero de dias de atraso e num valor por dia. esse valor por dia deve ser facilmente parametrizavel nos sistema (ex., bootstrap ou ficheiro de propriedades)