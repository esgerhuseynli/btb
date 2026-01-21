import 'package:equatable/equatable.dart';
import '../../../data/models/bank_card.dart';
import '../../../data/models/bank_account.dart';
import '../../../data/models/transaction_data.dart';

abstract class HomeState extends Equatable {
  const HomeState();

  @override
  List<Object?> get props => [];
}

class HomeInitial extends HomeState {
  const HomeInitial();
}

class HomeLoading extends HomeState {
  const HomeLoading();
}

class HomeLoaded extends HomeState {
  final List<BankCard> bankCards;
  final List<BankAccount> bankAccounts;
  final List<TransactionData> transactions;
  final double totalBalance;
  final String userName;
  final String? userAvatarUrl;

  const HomeLoaded({
    required this.bankCards,
    required this.bankAccounts,
    this.transactions = const [],
    this.totalBalance = 0.0,
    this.userName = '',
    this.userAvatarUrl,
  });

  @override
  List<Object?> get props => [
        bankCards,
        bankAccounts,
        transactions,
        totalBalance,
        userName,
        userAvatarUrl,
      ];
}

class HomeError extends HomeState {
  final String message;

  const HomeError(this.message);

  @override
  List<Object?> get props => [message];
}



